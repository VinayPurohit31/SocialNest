package com.vsp.socialnest.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.vsp.socialnest.Models.Post
import com.vsp.socialnest.Models.User
import com.vsp.socialnest.R
import com.vsp.socialnest.adapters.FollowAdapter
import com.vsp.socialnest.adapters.PostAdapter
import com.vsp.socialnest.databinding.FragmentHomeBinding
import com.vsp.socialnest.utils.FOLLOW
import com.vsp.socialnest.utils.POST

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private var postList=ArrayList<Post>()
    private var followList=ArrayList<User>() // Changed to ArrayList<String>
    private lateinit var followAdapter: FollowAdapter
    private lateinit var adapter: PostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        adapter = PostAdapter(requireContext(), postList)
        binding.postRv.layoutManager = LinearLayoutManager(requireContext())
        binding.postRv.adapter = adapter

        followAdapter = FollowAdapter(requireContext(), followList) // Initialize adapter first
        binding.rvv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvv.adapter = followAdapter

        Firebase.firestore.collection(Firebase.auth.currentUser!!.uid + FOLLOW).get().addOnSuccessListener {
            val tempList = ArrayList<User>() // Changed to ArrayList<String>
            followList.clear()
            for (i in it.documents) {
                val user: User = i.toObject<User>()!!
                tempList.add(user) // Add user ID to tempList
            }
            followList.addAll(tempList)
            followAdapter.notifyDataSetChanged()
        }

        setHasOptionsMenu(true)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)

        Firebase.firestore.collection(POST).get().addOnSuccessListener {
            val tempList = arrayListOf<Post>()
            postList.clear()
            for (i in it.documents) {
                val post: Post = i.toObject<Post>()!!
                tempList.add(post)
            }
            postList.addAll(tempList)
            adapter.notifyDataSetChanged()
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.option_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}