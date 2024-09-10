package com.vsp.socialnest

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import com.vsp.socialnest.Models.User
import com.vsp.socialnest.databinding.ActivityCreateAccountBinding
import com.vsp.socialnest.utils.USER_NODE
import com.vsp.socialnest.utils.USER_PROFILE_FOLDER
import com.vsp.socialnest.utils.uploadImage

class CreateAccount : AppCompatActivity() {

    private lateinit var user: User
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
        user = User()

        if (intent.hasExtra("MODE") && intent.getIntExtra("MODE", -1) == 1) {
            setupProfileForUpdate()
        }

        binding.RegistarBtn.setOnClickListener {
            if (intent.hasExtra("MODE") && intent.getIntExtra("MODE", -1) == 1) {
                updateProfile()
            } else {
                createNewAccount()
            }
        }

        binding.Login.setOnClickListener {
            startActivity(Intent(this@CreateAccount, Login::class.java))
            finish()
        }

        binding.AddProfileImage.setOnClickListener {
            launcher.launch("image/*")
        }

        adjustInsets()
    }

    private fun setupProfileForUpdate() {
        binding.RegistarBtn.text = "Update Profile"
        Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid).get()
            .addOnSuccessListener {
                user = it.toObject<User>()!!
                binding.NAME.editText?.setText(user.name)
                binding.EMAIL.editText?.setText(user.email)
                binding.PASSWORD.editText?.setText(user.password)
                if (!user.image.isNullOrEmpty()) {
                    Picasso.get().load(user.image).into(binding.ProfileImage)
                }
            }
    }

    private fun updateProfile() {
        Firebase.firestore.collection(USER_NODE)
            .document(Firebase.auth.currentUser!!.uid)
            .set(user)
            .addOnSuccessListener {
                Toast.makeText(this@CreateAccount, "Account updated successfully", Toast.LENGTH_SHORT).show()
                navigateToHomeScreen()
            }
    }

    private fun createNewAccount() {
        val name = binding.NAME.editText?.text.toString()
        val email = binding.EMAIL.editText?.text.toString()
        val password = binding.PASSWORD.editText?.text.toString()

        if (name.isEmpty()) {
            binding.NAME.error = "Please enter a name"
            return
        } else {
            binding.NAME.error = null
        }

        if (email.isEmpty()) {
            binding.EMAIL.error = "Please enter an email"
            return
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.EMAIL.error = "Please enter a valid email"
            return
        } else {
            binding.EMAIL.error = null
        }

        if (password.isEmpty()) {
            binding.PASSWORD.error = "Please enter a password"
            return
        } else if (password.length < 6) {
            binding.PASSWORD.error = "Password must be at least 6 characters"
            return
        } else {
            binding.PASSWORD.error = null
        }

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
                            navigateToHomeScreen()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this@CreateAccount, "Account creation failed", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(this@CreateAccount, "Account creation failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun navigateToHomeScreen() {
        startActivity(Intent(this@CreateAccount, HomeScreen::class.java))
        finish()
    }

    private fun adjustInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
