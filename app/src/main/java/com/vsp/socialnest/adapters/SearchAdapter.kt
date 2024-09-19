package com.vsp.socialnest.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.vsp.socialnest.Models.User
import com.vsp.socialnest.R
import com.vsp.socialnest.databinding.SearchRvBinding
import com.vsp.socialnest.utils.FOLLOW
import com.vsp.socialnest.utils.USER_NODE

class SearchAdapter(var context: Context, var userList: ArrayList<User>): RecyclerView.Adapter<SearchAdapter.ViewHolder>() {
    inner class ViewHolder(var binding: SearchRvBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding = SearchRvBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context).load(userList.get(position).image).placeholder(R.drawable.display_pic)
            .into(holder.binding.ProfileImage)
        holder.binding.Name.text = userList.get(position).name

        Firebase.firestore.collection(Firebase.auth.currentUser!!.uid + FOLLOW)
            .whereEqualTo("email", userList.get(position).email).get().addOnSuccessListener {
                if (it.documents.size==0)  {
                    // Not following, set button to "Follow"
                    holder.binding.FollowBtn.text = "Follow"
                    holder.binding.FollowBtn.icon =
                        ContextCompat.getDrawable(context, R.drawable.add_circle_24px)
                } else {
                    // Following, set button to "Unfollow"
                    holder.binding.FollowBtn.text = "Unfollow"
                    holder.binding.FollowBtn.icon =
                        ContextCompat.getDrawable(context, R.drawable.cancel_24px)
                }
            }

        holder.binding.FollowBtn.setOnClickListener {
            val user = userList[position]
            Firebase.firestore.collection(Firebase.auth.currentUser!!.uid + FOLLOW)
                .whereEqualTo("email", user.email).get().addOnSuccessListener {
                    if (it.isEmpty) {
                        // Follow the user
                        Firebase.firestore.collection(Firebase.auth.currentUser!!.uid + FOLLOW)
                            .document().set(user)
                            .addOnSuccessListener {
                                holder.binding.FollowBtn.text = "Unfollow"
                                holder.binding.FollowBtn.icon =
                                    ContextCompat.getDrawable(context, R.drawable.cancel_24px)
                            }
                            .addOnFailureListener {
                                // Handle error
                            }
                    } else {
                        // Unfollow the user
                        for (document in it) {
                            Firebase.firestore.collection(Firebase.auth.currentUser!!.uid + FOLLOW)
                                .document(document.id).delete()
                                .addOnSuccessListener {
                                    holder.binding.FollowBtn.text = "Follow"
                                    holder.binding.FollowBtn.icon =
                                        ContextCompat.getDrawable(context, R.drawable.add_circle_24px)
                                }
                                .addOnFailureListener {
                                    // Handle error
                                }
                        }
                    }
                }
        }
    }
}