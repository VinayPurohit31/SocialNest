package com.vsp.socialnest.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.vsp.socialnest.Models.User
import com.vsp.socialnest.R
import com.vsp.socialnest.adapters.SearchAdapter
import com.vsp.socialnest.databinding.FragmentMyMemoriesBinding
import com.vsp.socialnest.databinding.FragmentSearchBinding
import com.vsp.socialnest.utils.USER_NODE

class SearchFragment : Fragment() {
    lateinit var binding: FragmentSearchBinding
    lateinit var adapter: SearchAdapter
    var userList=ArrayList<User>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentSearchBinding.inflate(inflater,container,false)
        binding.rv.layoutManager=LinearLayoutManager(requireContext())
        adapter= SearchAdapter(requireContext(),userList)
        binding.rv.adapter=adapter

        Firebase.firestore.collection(USER_NODE).get().addOnSuccessListener {
            var tempList=ArrayList<User>()
            userList.clear()
            for (i in it.documents){
                if (i.id==Firebase.auth.currentUser!!.uid)
                {

                }
                else{
                    var user: User =i.toObject<User>()!!
                    tempList.add(user)
                }


            }
            userList.addAll(tempList)
            adapter.notifyDataSetChanged() // Notify adapter about data change
        }

        return binding.root
    }

    companion object {

    }
}