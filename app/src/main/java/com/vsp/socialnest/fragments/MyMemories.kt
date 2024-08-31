package com.vsp.socialnest.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.vsp.socialnest.Models.Post
import com.vsp.socialnest.R
import com.vsp.socialnest.adapters.MyPostRvAdapter
import com.vsp.socialnest.databinding.FragmentMyMemoriesBinding

class MyMemories : Fragment() {
   private lateinit var binding: FragmentMyMemoriesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentMyMemoriesBinding.inflate(inflater,container,false)
        val postList = ArrayList<Post>() // Initialize postList here
        val adapter = MyPostRvAdapter(requireContext(), postList) // Pass postList to adapter
        binding.rv.layoutManager= StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL)
        binding.rv.adapter=adapter
        Firebase.firestore.collection(Firebase.auth.currentUser!!.uid).get().addOnSuccessListener {
            var tempList=ArrayList<Post>()
            for (i in it.documents){
               var post:Post=i.toObject<Post>()!!
                tempList.add(post)
            }
            postList.addAll(tempList)
            adapter.notifyDataSetChanged()
        }
        return binding.root
    }

    companion object {

    }
}