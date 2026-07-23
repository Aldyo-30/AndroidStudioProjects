package com.example.tas_mobile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.tas_mobile.ui.fragments.AddSpotFragment
import com.example.tas_mobile.ui.fragments.BookmarkFragment
import com.example.tas_mobile.ui.fragments.ExploreFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_explore -> loadFragment(ExploreFragment())
                R.id.nav_add_spot -> loadFragment(AddSpotFragment())
                R.id.nav_favorites -> loadFragment(BookmarkFragment())
            }
            true
        }

        // Initial fragment
        if (savedInstanceState == null) {
            loadFragment(ExploreFragment())
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}