package com.vsp.socialnest.adapters

import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso

import com.vsp.socialnest.Models.Pops
import com.vsp.socialnest.R
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
            mediaPlayer ->
            mediaPlayer.isLooping= true
            holder.binding.progressBar.visibility=View.GONE
            holder.binding.videoView.start()
        }
        holder.binding.likeButton.setOnClickListener {
            val likedDrawable = ContextCompat.getDrawable(context, R.drawable.favourate_filled)
            val unlikedDrawable = ContextCompat.getDrawable(context, R.drawable.favorite_24px) // Replace with your unliked drawable

            if (holder.binding.likeButton.drawable.constantState == likedDrawable?.constantState) {
                // Already liked, unlike it
                unlikedDrawable?.setColorFilter(ContextCompat.getColor(context, R.color.your_unliked_color), PorterDuff.Mode.SRC_IN) // Setdesired color for unliked state
                holder.binding.likeButton.setImageDrawable(unlikedDrawable)
            } else {
                // Like it
                likedDrawable?.setColorFilter(ContextCompat.getColor(context, R.color.red), PorterDuff.Mode.SRC_IN)
                holder.binding.likeButton.setImageDrawable(likedDrawable)
            }
        }
        holder.binding.shareButton.setOnClickListener{
            var i= Intent(Intent.ACTION_SEND)
            i.setType("text/plain")
            i.putExtra(Intent.EXTRA_TEXT,popsList.get(position).popsUrl)
            context.startActivity(i)
        }
    }

    override fun getItemCount(): Int {
        return popsList.size
    }
}
