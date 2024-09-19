package com.vsp.socialnest.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.vsp.socialnest.Models.User
import com.vsp.socialnest.R
import com.vsp.socialnest.adapters.SearchAdapter
import com.vsp.socialnest.databinding.FragmentSearchBinding
import com.vsp.socialnest.utils.USER_NODE

class SearchFragment : Fragment() {
    lateinit var binding: FragmentSearchBinding
    lateinit var adapter: SearchAdapter
    var userList = ArrayList<User>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        // Set up RecyclerView
        binding.rv.layoutManager = LinearLayoutManager(requireContext())
        adapter = SearchAdapter(requireContext(), userList)
        binding.rv.adapter = adapter

        // Fetch all users on initial load
        Firebase.firestore.collection(USER_NODE).get().addOnSuccessListener {
            val tempList = ArrayList<User>()
            userList.clear()
            for (document in it.documents) {
                if (document.id != Firebase.auth.currentUser!!.uid) {
                    val user: User = document.toObject<User>()!!
                    tempList.add(user)
                }
            }
            userList.addAll(tempList)
            adapter.notifyDataSetChanged() // Notify adapter about data change
        }

        // Search button click listener
        binding.searchBtn.setOnClickListener {
            val searchText = binding.searchEt.text.toString().trim()

            if (searchText.isNotEmpty()) {
                // Query Firestore for users matching the search term
                Firebase.firestore.collection(USER_NODE)
                    .whereEqualTo("name", searchText)
                    .get()
                    .addOnSuccessListener { documents ->
                        val tempList = ArrayList<User>()
                        userList.clear()
                        for (document in documents) {
                            if (document.id != Firebase.auth.currentUser!!.uid) {
                                val user: User = document.toObject<User>()!!
                                tempList.add(user)
                            }
                        }
                        // Update the list with the filtered search results
                        userList.addAll(tempList)
                        adapter.notifyDataSetChanged() // Refresh RecyclerView with new data
                    }
                    .addOnFailureListener {
                        // Handle error case (optional)
                    }
            } else {
                // Optionally, show a message for empty search
            }
        }

        return binding.root
    }
}
