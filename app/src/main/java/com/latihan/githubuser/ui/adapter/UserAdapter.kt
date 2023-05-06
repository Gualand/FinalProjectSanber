package com.latihan.githubuser.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.latihan.githubuser.ItemsItem
import com.latihan.githubuser.R
import de.hdodenhof.circleimageview.CircleImageView

class UserAdapter(private val userList: List<ItemsItem>) :
    RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(data: ItemsItem)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvItemUsername: TextView = view.findViewById(R.id.txtvItemUsername)
        val imgItemProfile: CircleImageView = view.findViewById(R.id.imgvItemProfile)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(viewGroup.context).inflate(R.layout.item_user, viewGroup, false)
    )

    override fun getItemCount(): Int = userList.size

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val username = userList[position].login
        val avatarUrl = userList[position].avatarUrl

        viewHolder.tvItemUsername.text = username
        Glide.with(viewHolder.itemView.context).load(avatarUrl).into(viewHolder.imgItemProfile)
        viewHolder.itemView.setOnClickListener {onItemClickCallback.onItemClicked(userList[viewHolder.adapterPosition])}
    }
}

