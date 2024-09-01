package com.vsp.socialnest.Post

import android.content.Intent
import android.os.Bundle
import android.graphics.Color
import android.graphics.PorterDuff
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.vsp.socialnest.HomeScreen
import com.vsp.socialnest.Models.Post
import com.vsp.socialnest.R
import com.vsp.socialnest.databinding.ActivityMemoryBinding
import com.vsp.socialnest.utils.MEMORIES_FOLDER
import com.vsp.socialnest.utils.POST
import com.vsp.socialnest.utils.USER_PROFILE_FOLDER
import com.vsp.socialnest.utils.uploadImage

class MemoriesActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMemoryBinding.inflate(layoutInflater)
    }
    var imageUrl: String? = null
    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            uploadImage(it, MEMORIES_FOLDER) { url ->
                if (url != null) {
                    binding.MemoriesImage.setImageURI(uri)
                    imageUrl = url
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root) //Set the content view to the root of the layout

        setSupportActionBar(binding.materialToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // Change navigation icon color
        val drawable = binding.materialToolbar.navigationIcon
        if (drawable != null) {
            drawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
            binding.materialToolbar.navigationIcon = drawable
        }

        supportActionBar?.setDisplayShowHomeEnabled(true)

        binding.materialToolbar.setNavigationOnClickListener {
            finish()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.MemoriesImage.setOnClickListener {
            launcher.launch("image/*")
        }
        binding.CancelBtn.setOnClickListener {
            startActivity(Intent(this@MemoriesActivity, HomeScreen::class.java))
            finish()
        }
        binding.PostBtn.setOnClickListener {
            if (imageUrl != null) {
                val post: Post = Post(imageUrl!!, binding.caption.editText?.text.toString())

                Firebase.firestore.collection(POST).document().set(post).addOnSuccessListener {
                    Firebase.firestore.collection(Firebase.auth.currentUser!!.uid)
                        .document().set(post).addOnSuccessListener {
                            startActivity(Intent(this@MemoriesActivity, HomeScreen::class.java))
                            finish()
                        }
                }
            } else {
                Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
            }
        }
    }
}