package com.example.tts

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.tts.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var currentFragmentTag: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (savedInstanceState != null) {
            currentFragmentTag = savedInstanceState.getString(KEY_FRAGMENT_TAG)
            val fragment = supportFragmentManager.findFragmentByTag(currentFragmentTag)
            if (fragment != null) {
                // Fragment already exists, it will be automatically re-attached
            }
        } else {
            // First time, load default fragment
            loadFragment(DashboardFragment(), TAG_DASHBOARD)
        }

        (binding.navigation as NavigationBarView).setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_dashboard -> {
                    loadFragment(DashboardFragment(), TAG_DASHBOARD)
                    true
                }
                R.id.navigation_account -> {
                    loadFragment(AccountFragment(), TAG_ACCOUNT)
                    true
                }
                else -> false
            }
        }

        binding.btnLanguage.setOnClickListener {
            val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(intent)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_FRAGMENT_TAG, currentFragmentTag)
    }

    private fun loadFragment(fragment: Fragment, tag: String) {
        if (currentFragmentTag == tag) return
        
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, fragment, tag)
            .commit()
        
        currentFragmentTag = tag
    }

    companion object {
        private const val KEY_FRAGMENT_TAG = "key_fragment_tag"
        private const val TAG_DASHBOARD = "tag_dashboard"
        private const val TAG_ACCOUNT = "tag_account"
    }
}