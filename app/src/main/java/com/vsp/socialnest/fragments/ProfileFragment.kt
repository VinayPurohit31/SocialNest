package com.vsp.socialnest.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import com.vsp.socialnest.CreateAccount
import com.vsp.socialnest.Models.User
import com.vsp.socialnest.adapters.ViewPagerAdapters
import com.vsp.socialnest.databinding.FragmentProfileBinding
import com.vsp.socialnest.utils.USER_NODE

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewPagerAdapter: ViewPagerAdapters

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding.editProfile.setOnClickListener(){
            val intent=Intent(activity, CreateAccount::class.java)
            intent.putExtra("MODE",1)
            activity?.startActivity(intent)
            activity?.finish()
        }
        viewPagerAdapter = ViewPagerAdapters(requireActivity().supportFragmentManager)
        viewPagerAdapter.addFragment(MyMemories(), "My Memories")
        viewPagerAdapter.addFragment(MyPops(), "My Pops")
        binding.viewPager.adapter = viewPagerAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        // Check if the user is signed in
        val currentUser = Firebase.auth.currentUser
        if (currentUser != null) {
            Firebase.firestore.collection(USER_NODE).document(currentUser.uid).get()
                .addOnSuccessListener { documentSnapshot ->
                    // Convert the document to a User object
                    val user: User? = documentSnapshot.toObject<User>()
                    if (user != null) {
                        // Update UI with user information
                        binding.name.text = user.name
                        binding.email.text = user.email
                        if (!user.image.isNullOrEmpty()) {
                            Picasso.get().load(user.image).into(binding.ProfileImage)
                        }
                    } else {
                        // Handle the case where the document doesn't exist or is empty
                        Toast.makeText(requireContext(), "User document not found", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle Firestore read failure (e.g., log or display a message)
                    Log.e("Firestore", "Error getting user document", exception)
                }
        } else {
            // Handle the case where the user is not signed in (e.g., redirect to login)
        }
    }
}
