package com.vsp.socialnest

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.vsp.socialnest.Models.User
import com.vsp.socialnest.databinding.ActivityCreateAccountBinding
import com.vsp.socialnest.utils.USER_NODE
import com.vsp.socialnest.utils.USER_PROFILE_FOLDER
import com.vsp.socialnest.utils.uploadImage

class CreateAccount : AppCompatActivity() {

    private lateinit var user: User // Lateinit to ensure it's initialized before use
    private val binding by lazy {
        ActivityCreateAccountBinding.inflate(layoutInflater)
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            uploadImage(it, USER_PROFILE_FOLDER) { imageUrl ->
                if (imageUrl == null) {
                    Toast.makeText(this@CreateAccount, "Something went wrong", Toast.LENGTH_SHORT).show()
                } else {
                    user.image = imageUrl
                    binding.ProfileImage.setImageURI(uri)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        user = User() // Initialize user object here

        binding.RegistarBtn.setOnClickListener {
            val name = binding.NAME.editText?.text.toString()
            val email = binding.EMAIL.editText?.text.toString()
            val password = binding.PASSWORD.editText?.text.toString()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this@CreateAccount, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            } else {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { result ->
                        if (result.isSuccessful) {
                            user.name = name
                            user.email = email
                            user.password = password

                            Firebase.firestore.collection(USER_NODE)
                                .document(Firebase.auth.currentUser!!.uid)
                                .set(user)
                                .addOnSuccessListener {
                                    Toast.makeText(this@CreateAccount, "Account created successfully", Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(this@CreateAccount,HomeScreen::class.java))
                                    finish()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(this@CreateAccount, "Account creation failed", Toast.LENGTH_SHORT).show()
                                }
                        } else {
                            Toast.makeText(this@CreateAccount, "Account creation failed", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
        binding.Login.setOnClickListener{
            startActivity(Intent(this@CreateAccount,Login::class.java))
            finish()
        }

        binding.AddProfileImage.setOnClickListener {
            launcher.launch("image/*")
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
