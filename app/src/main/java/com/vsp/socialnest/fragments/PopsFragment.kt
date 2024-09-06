package com.vsp.socialnest.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.vsp.socialnest.Models.Pops
import com.vsp.socialnest.adapters.PopsAdapter
import com.vsp.socialnest.databinding.FragmentPopsBinding
import com.vsp.socialnest.utils.POPS


class PopsFragment : Fragment() {
    private lateinit var binding: FragmentPopsBinding
    lateinit var adapter: PopsAdapter
    var popsList = ArrayList<Pops>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       binding=FragmentPopsBinding.inflate(inflater,container,false)
        adapter=PopsAdapter(requireContext(),popsList)
        binding.viewPager.adapter=adapter
        Firebase.firestore.collection(POPS).get().addOnSuccessListener {
            var tempList=ArrayList<Pops>()
            popsList.clear()
            for (i in it.documents){
                val pops = i.toObject(Pops::class.java)
                tempList.add(pops!!)
            }
            popsList.addAll(tempList)
            popsList.reverse()
            adapter.notifyDataSetChanged()
        }
        return binding.root
    }

    companion object {

    }
}