package com.vsp.socialnest

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.vsp.socialnest.Models.User
import com.vsp.socialnest.databinding.ActivityLoginBinding

class Login : AppCompatActivity() {
    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.Login.setOnClickListener {
            val email = binding.EMAIL.editText?.text.toString().trim()
            val password = binding.PASSWORD.editText?.text.toString().trim()

            // Validate email and password
            if (!isValidEmail(email)) {
                binding.EMAIL.error = "Invalid email format"
            } else if (!isValidPassword(password)) {
                binding.PASSWORD.error = "Password must be at least 6 characters"
            } else {
                // Clear any previous errors
                binding.EMAIL.error = null
                binding.PASSWORD.error = null

                // Proceed with Firebase authentication
                val user = User(email, password)

                Firebase.auth.signInWithEmailAndPassword(user.email!!, user.password!!)
                    .addOnSuccessListener {
                        if (it.user != null) {
                            startActivity(Intent(this, HomeScreen::class.java))
                            finish()
                        } else {
                            Toast.makeText(this@Login, "Login failed. Please try again.", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(this@Login, it.localizedMessage, Toast.LENGTH_SHORT).show()
                    }
            }
        }

        // Set click listener for Create Account button
        binding.CreateAccount.setOnClickListener {
            startActivity(Intent(this@Login, CreateAccount::class.java))
        }

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun enableEdgeToEdge() {
        // Add your edge-to-edge handling logic here
    }

    // Email validation function
    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // Password validation function
    private fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }
}
