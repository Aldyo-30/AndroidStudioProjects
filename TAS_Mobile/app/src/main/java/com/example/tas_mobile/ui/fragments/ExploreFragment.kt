package com.example.tas_mobile.ui.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.tas_mobile.R
import com.example.tas_mobile.models.CitizenSpot
import com.example.tas_mobile.models.TouristResponse
import com.example.tas_mobile.models.TouristSpot
import com.example.tas_mobile.network.RetrofitClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExploreFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private val officialSpots = mutableListOf<TouristSpot>()
    private val citizenSpots = mutableListOf<TouristSpot>()
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    private val db = FirebaseDatabase.getInstance().getReference("citizen_spots")
    private val bookmarksDb = FirebaseDatabase.getInstance().getReference("bookmarks")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_explore, container, false)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        setupBottomSheet(view)
        setupFilter(view)
        setupMyLocation(view)

        return view
    }

    private fun setupBottomSheet(view: View) {
        val bottomSheet = view.findViewById<View>(R.id.bottom_sheet)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private fun setupFilter(view: View) {
        val spinner = view.findViewById<Spinner>(R.id.spinner_filter)
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.filter_options,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                updateMarkers(position)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setupMyLocation(view: View) {
        view.findViewById<View>(R.id.fab_my_location).setOnClickListener {
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
                return@setOnClickListener
            }
            mMap.isMyLocationEnabled = true
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    val latLng = LatLng(it.latitude, it.longitude)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                }
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val salatiga = LatLng(-7.3305, 110.5084)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(salatiga, 13f))

        mMap.setOnMarkerClickListener { marker ->
            val spot = marker.tag as? TouristSpot
            spot?.let { showSpotDetail(it) }
            false
        }

        fetchOfficialSpots()
        listenCitizenSpots()
    }

    private fun fetchOfficialSpots() {
        RetrofitClient.instance.getOfficialSpots().enqueue(object : Callback<TouristResponse> {
            override fun onResponse(call: Call<TouristResponse>, response: Response<TouristResponse>) {
                if (response.isSuccessful) {
                    officialSpots.clear()
                    officialSpots.addAll(response.body()?.dataWisata ?: emptyList())
                    
                    val spinner = view?.findViewById<Spinner>(R.id.spinner_filter)
                    updateMarkers(spinner?.selectedItemPosition ?: 0)
                }
            }
            override fun onFailure(call: Call<TouristResponse>, t: Throwable) {
                Toast.makeText(context, "Failed to load official spots", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun listenCitizenSpots() {
        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                citizenSpots.clear()
                for (child in snapshot.children) {
                    val spot = child.getValue(CitizenSpot::class.java)
                    spot?.let { citizenSpots.add(it.toTouristSpot()) }
                }
                
                val spinner = view?.findViewById<Spinner>(R.id.spinner_filter)
                updateMarkers(spinner?.selectedItemPosition ?: 0)
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun updateMarkers(filterPosition: Int) {
        if (!::mMap.isInitialized) return
        mMap.clear()

        // 0: Semua, 1: Wisata Resmi, 2: Spot Warga
        if (filterPosition == 0 || filterPosition == 1) {
            officialSpots.forEach { addMarker(it, BitmapDescriptorFactory.HUE_BLUE) }
        }
        if (filterPosition == 0 || filterPosition == 2) {
            citizenSpots.forEach { addMarker(it, BitmapDescriptorFactory.HUE_GREEN) }
        }
    }

    private fun addMarker(spot: TouristSpot, color: Float) {
        val marker = mMap.addMarker(
            MarkerOptions()
                .position(LatLng(spot.latitude, spot.longitude))
                .title(spot.nama)
                .icon(BitmapDescriptorFactory.defaultMarker(color))
        )
        marker?.tag = spot
    }

    private fun showSpotDetail(spot: TouristSpot) {
        val view = requireView()
        view.findViewById<TextView>(R.id.tv_spot_name).text = spot.nama
        view.findViewById<TextView>(R.id.tv_spot_address).text = spot.alamat
        view.findViewById<TextView>(R.id.tv_spot_rating).text = "★ ${spot.rating}"
        view.findViewById<TextView>(R.id.tv_spot_category_label).text = spot.kategori
        
        val badge = view.findViewById<TextView>(R.id.tv_badge)
        if (spot.isOfficial) {
            badge.text = "WISATA RESMI"
            badge.setBackgroundColor(Color.parseColor("#BBDEFB"))
            badge.setTextColor(Color.parseColor("#1976D2"))
        } else {
            badge.text = "SPOT WARGA"
            badge.setBackgroundColor(Color.parseColor("#C8E6C9"))
            badge.setTextColor(Color.parseColor("#388E3C"))
        }

        view.findViewById<View>(R.id.btn_bookmark_icon).setOnClickListener {
            checkAndSaveToBookmark(spot)
        }

        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun checkAndSaveToBookmark(spot: TouristSpot) {
        val spotId = spot.idWisata.ifEmpty { spot.hashCode().toString() }
        
        bookmarksDb.child(spotId).get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                Toast.makeText(context, "Tempat ini sudah di favoritkan", Toast.LENGTH_SHORT).show()
            } else {
                saveToBookmark(spot, spotId)
            }
        }.addOnFailureListener {
            // Fallback to saving if check fails
            saveToBookmark(spot, spotId)
        }
    }

    private fun saveToBookmark(spot: TouristSpot, spotId: String) {
        bookmarksDb.child(spotId)
            .setValue(spot)
            .addOnSuccessListener {
                Toast.makeText(context, "Saved to favorites", Toast.LENGTH_SHORT).show()
            }
    }
}