package com.example.try1

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.try1.databinding.ActivityMainBinding





class MainActivity : AppCompatActivity() {

//    private lateinit var editTextUsername: EditText
//    private lateinit var editTextPassword: EditText
//    private lateinit var buttonLogin: Button

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
//App Register
        binding.buttonRegister.setOnClickListener {
            val username = binding.editTextUsername.text.toString()
            val password = binding.editTextPassword.text.toString()
            val confirmpassword = binding.editConfirmPassword.text.toString()
            val fullname = binding.editTextFullname.text.toString()

            if (username.isEmpty() || password.isEmpty() || fullname.isEmpty()) {
                Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show()

            }
            else if (password != confirmpassword) {
                Toast.makeText(this, "Password tidak sama", Toast.LENGTH_SHORT).show()

            }

            else {Toast.makeText(this, "Register Succesfull", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java)
                intent.putExtra("username", username)
                intent.putExtra("password", password)
                intent.putExtra("fullname", fullname)
                startActivity(intent)
                this.finish() //ditutup untuk registernya
            }
        }
//        editTextUsername = findViewById<EditText>(R.id.editTextUsername)
//        editTextPassword = findViewById<EditText>(R.id.editTextPassword)
//        buttonLogin = findViewById<Button>(R.id.buttonLogin)

//        binding.editTextUsername.setText("Aldyo")
//        binding.editTextPassword.setText("123")
//        binding.buttonRegister.setOnClickListener {
//            val username = binding.editTextUsername.text.toString()
//            val password = binding.editTextPassword.text.toString()
//            Toast.makeText(this, username + " " + password, Toast.LENGTH_SHORT).show()
//
//        }




    }
}