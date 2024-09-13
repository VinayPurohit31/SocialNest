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
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.vsp.socialnest.Models.Post
import com.vsp.socialnest.R
import com.vsp.socialnest.adapters.PostAdapter
import com.vsp.socialnest.databinding.FragmentHomeBinding
import com.vsp.socialnest.utils.POST

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private var postList=ArrayList<Post>()
    private lateinit var adapter: PostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)  // Ensure the fragment can provide its own menu
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        adapter = PostAdapter(requireContext(), postList)
        binding.postRv.layoutManager= LinearLayoutManager(requireContext()) // Corrected line
        binding.postRv.adapter = adapter // Set the adapter for the RecyclerView

        setHasOptionsMenu(true)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)

        Firebase.firestore.collection(POST).get().addOnSuccessListener {
            var tempList= arrayListOf<Post>()
            postList.clear()
            for (i in it.documents) {
                var post: Post = i.toObject<Post>()!!
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
