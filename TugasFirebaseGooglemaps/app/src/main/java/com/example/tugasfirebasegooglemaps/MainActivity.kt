package com.example.tugasfirebasegooglemaps

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tugasfirebasegooglemaps.databinding.ActivityMainBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlin.random.Random

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mMap: GoogleMap
    private lateinit var database: DatabaseReference
    
    private var isMapInteractionEnabled = true
    private var initialMarker: Marker? = null
    private var isFirstSave = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase
        database = FirebaseDatabase.getInstance().getReference("location")

        // Initialize Map
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.btnSave.setOnClickListener {
            saveData()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.setOnMapClickListener { latLng ->
            if (isMapInteractionEnabled) {
                // Update EditTexts
                binding.etLatitude.setText(latLng.latitude.toString())
                binding.etLongitude.setText(latLng.longitude.toString())

                // Manage initial marker (can be moved/replaced until Save is clicked)
                initialMarker?.remove()
                initialMarker = mMap.addMarker(
                    MarkerOptions()
                        .position(latLng)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                )
                
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
            } else {
                Toast.makeText(this, "Map selection disabled.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addMarker(latLng: LatLng, color: Float) {
        mMap.addMarker(
            MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.defaultMarker(color))
        )
        // Zoom only for new markers added via Save
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5f))
    }

    private fun saveData() {
        val latStr = binding.etLatitude.text.toString()
        val lngStr = binding.etLongitude.text.toString()

        if (latStr.isNotEmpty() && lngStr.isNotEmpty()) {
            val lat = latStr.toDouble()
            val lng = lngStr.toDouble()
            val latLng = LatLng(lat, lng)

            if (isFirstSave) {
                // Confirm the location picked from map
                isMapInteractionEnabled = false
                isFirstSave = false

                initialMarker?.let {
                    // Update its position in case user edited EditTexts before first save
                    it.position = latLng
                }
                
                Toast.makeText(this, "Location confirmed. Map tap disabled.", Toast.LENGTH_SHORT).show()
            } else {
                // Add a NEW marker with a DIFFERENT color
                val randomColor = getRandomColor()
                addMarker(latLng, randomColor)
            }

            // Update Firebase
            updateFirebase(latLng)
            Toast.makeText(this, "Data Saved to Firebase", Toast.LENGTH_SHORT).show()
            
        } else {
            Toast.makeText(this, "Please select a location on the map", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateFirebase(latLng: LatLng) {
        val data = mapOf(
            "latitude" to latLng.latitude,
            "longitude" to latLng.longitude
        )
        database.setValue(data)
    }

    private fun getRandomColor(): Float {
        // Range 30-330 avoids the Red area (0/360) and Azure area (~210)
        var hue: Float
        do {
            hue = Random.nextInt(0, 360).toFloat()
        } while (hue < 20 || (hue > 190 && hue < 230) || hue > 340) 
        return hue
    }
}
