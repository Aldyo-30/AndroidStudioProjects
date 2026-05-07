package com.example.try1

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.try1.databinding.ActivityLoginBinding
import com.example.try1.databinding.ActivityMainBinding

class LoginActivity : AppCompatActivity() {


    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.buttonLogin.setOnClickListener {
            val intent = intent
            val username =intent.getStringExtra("username")
            val password = intent.getStringExtra("password")
            val fullname = intent.getStringExtra("fullname")
            val usernameInput = binding.editTextUsername.text.toString()
            val passwordInput = binding.editTextPassword.text.toString()
            if (usernameInput.isEmpty() || passwordInput.isEmpty()) {
                Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show()

            }
            else if (usernameInput == username && passwordInput == password) {
                Toast.makeText(this, "Login Succesfull", Toast.LENGTH_SHORT).show()
                val intent = android.content.Intent(this, WelcomeActivity::class.java)
                intent.putExtra("fullname", fullname)
                startActivity(intent)
                this.finish()
            }else{
                Toast.makeText(this, "salah username or password", Toast.LENGTH_SHORT).show()
            }
        }



    }
}