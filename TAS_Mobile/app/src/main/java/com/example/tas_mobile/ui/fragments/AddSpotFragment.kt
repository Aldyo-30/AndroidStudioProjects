package com.example.tas_mobile.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.tas_mobile.R
import com.example.tas_mobile.models.CitizenSpot
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddSpotFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var tvCoordinates: TextView
    private lateinit var etName: EditText
    private lateinit var etDescription: EditText
    private lateinit var rgCategory: RadioGroup
    private var selectedLatLng: LatLng? = null

    private val db = FirebaseDatabase.getInstance().getReference("citizen_spots")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_spot, container, false)

        tvCoordinates = view.findViewById(R.id.tv_coordinates)
        etName = view.findViewById(R.id.et_name)
        etDescription = view.findViewById(R.id.et_description)
        rgCategory = view.findViewById(R.id.rg_category)

        val mapFragment = childFragmentManager.findFragmentById(R.id.add_map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        view.findViewById<Button>(R.id.btn_submit).setOnClickListener {
            submitSpot()
        }

        return view
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val salatiga = LatLng(-7.3305, 110.5084)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(salatiga, 13f))

        mMap.setOnCameraIdleListener {
            val center = mMap.cameraPosition.target
            selectedLatLng = center
            tvCoordinates.text = "* Koordinat terpilih: ${center.latitude}, ${center.longitude}"
        }
    }

    private fun submitSpot() {
        val name = etName.text.toString().trim()
        val description = etDescription.text.toString().trim()
        val latLng = selectedLatLng

        if (name.isEmpty() || description.isEmpty() || latLng == null) {
            Toast.makeText(context, "Please fill all fields and select location", Toast.LENGTH_SHORT).show()
            return
        }

        val categoryId = rgCategory.checkedRadioButtonId
        val category = when (categoryId) {
            R.id.rb_kuliner -> "KULINER"
            R.id.rb_fotografi -> "FOTOGRAFI"
            R.id.rb_taman -> "TAMAN / ALAM"
            else -> "UMUM"
        }

        val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val timestamp = System.currentTimeMillis()
        val customId = "spot_$timestamp"
        
        val spot = CitizenSpot(
            id = customId,
            nama_spot = name,
            kategori = category,
            deskripsi = description,
            latitude = latLng.latitude,
            longitude = latLng.longitude,
            ditambahkan_pada = date
        )

        db.child(customId).setValue(spot).addOnSuccessListener {
            Toast.makeText(context, "Spot added successfully", Toast.LENGTH_SHORT).show()
            clearForm()
        }.addOnFailureListener {
            Toast.makeText(context, "Failed to add spot", Toast.LENGTH_SHORT).show()
        }
    }

    private fun clearForm() {
        etName.text.clear()
        etDescription.text.clear()
        rgCategory.clearCheck()
    }
}