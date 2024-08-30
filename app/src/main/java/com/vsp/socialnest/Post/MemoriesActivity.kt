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
import com.vsp.socialnest.HomeScreen
import com.vsp.socialnest.R
import com.vsp.socialnest.databinding.ActivityMemoryBinding
import com.vsp.socialnest.utils.USER_PROFILE_FOLDER
import com.vsp.socialnest.utils.uploadImage

class MemoriesActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMemoryBinding.inflate(layoutInflater)
    }
    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            uploadImage(it, USER_PROFILE_FOLDER) { imageUrl ->
                if (imageUrl != null) {
//                    Toast.makeText(this@MemoriesActivity, "Successfully uploaded", Toast.LENGTH_SHORT).show()
//                    user.image = imageUrl
//                    binding.ProfileImage.setImageURI(uri)
                    binding.MemoriesImage.setImageURI(uri)
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
        val drawable = binding.materialToolbar.navigationIcon
        drawable?.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
        binding.materialToolbar.navigationIcon = drawable
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
    }
}