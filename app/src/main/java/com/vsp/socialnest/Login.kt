package com.vsp.socialnest

import android.content.Intent
import android.os.Bundle
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
            val email = binding.EMAIL.editText?.text.toString()
            val password = binding.PASSWORD.editText?.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this@Login, "Please fill all the details", Toast.LENGTH_SHORT).show()
            } else {
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
}
