package com.example.tts

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tts.databinding.FragmentAccountBinding

class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSaveProfile.setOnClickListener {
            saveProfile()
        }
    }

    private fun saveProfile() {
        val name = binding.etName.text.toString().trim()
        val nim = binding.etNim.text.toString().trim()
        val gpaString = binding.etGpa.text.toString().trim()

        var isValid = true

        if (name.isEmpty()) {
            binding.etName.error = getString(R.string.error_empty)
            isValid = false
        }

        if (nim.isEmpty()) {
            binding.etNim.error = getString(R.string.error_empty)
            isValid = false
        }

        if (gpaString.isEmpty()) {
            binding.etGpa.error = getString(R.string.error_empty)
            isValid = false
        } else {
            val gpa = gpaString.toDoubleOrNull()
            if (gpa == null || gpa < 0.0 || gpa > 4.0) {
                binding.etGpa.error = getString(R.string.error_invalid_gpa)
                isValid = false
            }
        }

        if (isValid) {
            val bundle = Bundle().apply {
                putString(DetailAccountActivity.EXTRA_NAME, name)
                putString(DetailAccountActivity.EXTRA_NIM, nim)
                putString(DetailAccountActivity.EXTRA_GPA, gpaString)
            }

            val intent = Intent(requireContext(), DetailAccountActivity::class.java).apply {
                putExtra(DetailAccountActivity.EXTRA_BUNDLE, bundle)
            }
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}