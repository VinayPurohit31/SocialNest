package com.vsp.socialnest.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.vsp.socialnest.Post.MemoriesActivity
import com.vsp.socialnest.Post.PopsActivity
import com.vsp.socialnest.R
import com.vsp.socialnest.databinding.FragmentAddBinding

class AddFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentAddBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       binding= FragmentAddBinding.inflate(inflater,container,false)
        binding.addMemory.setOnClickListener{
            activity?.startActivity(Intent(requireActivity(),MemoriesActivity::class.java))
            activity?.finish()
        }
        binding.addPops.setOnClickListener{
            activity?.startActivity(Intent(requireActivity(),PopsActivity::class.java))
        }
        return binding.root
    }

    companion object {

    }
}