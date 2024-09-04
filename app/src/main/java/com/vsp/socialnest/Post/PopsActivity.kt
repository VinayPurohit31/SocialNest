package com.vsp.socialnest.Post

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
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
import com.vsp.socialnest.Models.Pops
import com.vsp.socialnest.R
import com.vsp.socialnest.databinding.ActivityPopsBinding
import com.vsp.socialnest.utils.POPS
import com.vsp.socialnest.utils.POPS_FOLDER
import com.vsp.socialnest.utils.uploadVideo

class PopsActivity : AppCompatActivity() {

    val binding by lazy {
        ActivityPopsBinding.inflate(layoutInflater)
    }

//    var imageUrl: String? = null
    private lateinit var videoUrl: String
    lateinit var progressDialoge: ProgressDialog
    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            uploadVideo(it, POPS_FOLDER,progressDialoge) { url ->
                if (url != null) {
//                    binding.MemoriesImage.setImageURI(uri)
                    videoUrl = url
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        progressDialoge = ProgressDialog(this)
        binding.materialToolbar.setNavigationOnClickListener {
            startActivity(Intent(this@PopsActivity, HomeScreen::class.java))
            finish()
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.PopVideo.setOnClickListener {
            launcher.launch("video/*")
        }
        binding.CancelBtn.setOnClickListener {
            startActivity(Intent(this@PopsActivity, HomeScreen::class.java))
            finish()
        }
        binding.PostBtn.setOnClickListener {
            if (videoUrl != null) {
                val pops: Pops = Pops(videoUrl!!, binding.caption.editText?.text.toString())

                Firebase.firestore.collection(POPS).document().set(pops).addOnSuccessListener {
                    Firebase.firestore.collection(Firebase.auth.currentUser!!.uid+POPS)
                        .document().set(pops).addOnSuccessListener {
                            startActivity(Intent(this@PopsActivity, HomeScreen::class.java))
                            finish()
                        }
                }
            } else {
                Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
            }
        }

    }
}