package com.example.tts

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tts.databinding.ActivityDetailAccountBinding

class DetailAccountActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backAccount.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        supportActionBar?.apply {
            title = getString(R.string.title_detail_account)
            setDisplayHomeAsUpEnabled(true)
        }

        val bundle = intent.getBundleExtra(EXTRA_BUNDLE)
        if (bundle != null) {
            val name = bundle.getString(EXTRA_NAME)
            val nim = bundle.getString(EXTRA_NIM)
            val gpa = bundle.getString(EXTRA_GPA)

            binding.tvDisplayName.text = getString(R.string.display_name, name)
            binding.tvDisplayNim.text = getString(R.string.display_nim, nim)
            binding.tvDisplayGpa.text = getString(R.string.display_gpa, gpa)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    companion object {
        const val EXTRA_BUNDLE = "extra_bundle"
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_NIM = "extra_nim"
        const val EXTRA_GPA = "extra_gpa"
    }
}