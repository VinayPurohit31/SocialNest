package com.vsp.socialnest.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import com.squareup.picasso.R
import com.vsp.socialnest.Models.Pops
import com.vsp.socialnest.databinding.PopsDgBinding

class PopsAdapter(var context: Context, var popsList: ArrayList<Pops>) : RecyclerView.Adapter<PopsAdapter.ViewHolder>() {


    inner class ViewHolder(var binding: PopsDgBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val binding = PopsDgBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context).load(popsList[position].profileImage)
            .into(holder.binding.ProfileImage)
        holder.binding.caption.setText(popsList.get(position).caption)
        holder.binding.videoView.setVideoPath(popsList.get(position).popsUrl)
        holder.binding.videoView.setOnPreparedListener{
            holder.binding.videoView.start()
        }
    }

    override fun getItemCount(): Int {
        return popsList.size
    }
}
