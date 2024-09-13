package com.vsp.socialnest.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.vsp.socialnest.Models.Post
import com.vsp.socialnest.Models.User
import com.vsp.socialnest.R
import com.vsp.socialnest.databinding.MemoriesRvBinding
import com.vsp.socialnest.utils.USER_NODE

class PostAdapter(var context: Context, var postList: ArrayList<Post>):RecyclerView.Adapter<PostAdapter.MyHolder>() {
    inner class MyHolder(var binding: MemoriesRvBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        var binding = MemoriesRvBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyHolder(binding)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        try {
            val uid = postList[position].uid

            if (!uid.isNullOrEmpty()) {
                // Retrieve user data from Firestore using the post's uid
                Firebase.firestore.collection(USER_NODE)
                    .document(uid)
                    .get()
                    .addOnSuccessListener { documentSnapshot ->
                        if (documentSnapshot.exists()) {
                            // Map Firestore document to User object
                            val user = documentSnapshot.toObject<User>()
                            user?.let {
                                // Load user image and set name
                                Glide.with(context)
                                    .load(it.image)  // Ensure 'image' field exists in Firestore
                                    .placeholder(R.drawable.user)  // Default image
                                    .into(holder.binding.userPic)

                                holder.binding.name.text = it.name
                            }
                        } else {
                            // Handle case when user document doesn't exist
                            Log.e(
                                "PostAdapter",
                                "User document does not exist for UID: $uid"
                            )
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.e("PostAdapter", "Error loading user data", e)
                    }
            } else {
                Log.e("PostAdapter", "UID is null or empty for position: $position")
            }
        } catch (e: Exception) {
            Log.e("PostAdapter", "Exception in onBindViewHolder", e)
        }

        // Load post image and other details
        Glide.with(context)
            .load(postList[position].postUrl)
            .placeholder(R.drawable.loader)
            .into(holder.binding.postImage)

        holder.binding.caption.text = postList[position].caption
        holder.binding.time.text = postList[position].time
    }

}