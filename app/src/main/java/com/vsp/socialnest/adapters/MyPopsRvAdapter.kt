package com.vsp.socialnest.adapters
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.squareup.picasso.Picasso
import com.vsp.socialnest.Models.Pops
import com.vsp.socialnest.Models.Post
import com.vsp.socialnest.databinding.MyMemoriesRvDesignBinding

class MyPopsRvAdapter(var context: Context, var popsList:ArrayList<Pops>):RecyclerView.Adapter<MyPopsRvAdapter.ViewHolder>() {
    inner class ViewHolder(var binding: MyMemoriesRvDesignBinding):
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding=MyMemoriesRvDesignBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return popsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context)
            .load(popsList.get(position).popsUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.binding.postImage)
    }

}