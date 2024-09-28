package com.vsp.socialnest.adapters

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.marlonlom.utilities.timeago.TimeAgo
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.vsp.socialnest.Models.Post
import com.vsp.socialnest.Models.User
import com.vsp.socialnest.R
import com.vsp.socialnest.databinding.MemoriesRvBinding
import com.vsp.socialnest.utils.USER_NODE


class PostAdapter(var context: Context, var postList: ArrayList<Post>):RecyclerView.Adapter<PostAdapter.MyHolder>() {
    inner class MyHolder(var binding: MemoriesRvBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        var binding=MemoriesRvBinding.inflate(LayoutInflater.from(context),parent,false)
        return MyHolder(binding)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        try {
            Firebase.firestore.collection(USER_NODE).document(postList.get(position).uid).get().addOnSuccessListener {
                var user=it.toObject<User>()!!
               Glide.with(context).load(user!!.image).placeholder(R.drawable.user).into(holder.binding.userPic)
                holder.binding.name.text=user.name
            }
        }
        catch (e:Exception){}

        holder.binding.save.setOnClickListener {
            val imageUrl = postList[position].postUrl
            val request = DownloadManager.Request(Uri.parse(imageUrl))
                .setTitle("Downloading Image")
                .setDescription("Downloading image from post")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, "${System.currentTimeMillis()}.jpg")

            val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            downloadManager.enqueue(request)

            Toast.makeText(context, "Downloading image...", Toast.LENGTH_SHORT).show()
        }

        Glide.with(context).load(postList.get(position).postUrl).placeholder(R.drawable.loader).into(holder.binding.postImage)
        holder.binding.caption.text=postList.get(position).caption
        try {
            val text = TimeAgo.using(postList.get(position).time.toLong())
            holder.binding.time.text=text
        }
        catch (e:Exception){
            holder.binding.time.text="null"
        }
        holder.binding.share.setOnClickListener{
            var i=Intent(Intent.ACTION_SEND)
            i.setType("text/plain")
            i.putExtra(Intent.EXTRA_TEXT,postList.get(position).postUrl)
            context.startActivity(i)
        }

        holder.binding.like.setOnClickListener{
            holder.binding.like.setImageResource(R.drawable.heart_2)
        }

    }
}