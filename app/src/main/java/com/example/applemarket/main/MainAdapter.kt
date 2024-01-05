package com.example.applemarket.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.applemarket.Post
import com.example.applemarket.R
import com.example.applemarket.databinding.ItemMainPostBinding

class MainAdapter(private var data: MutableList<Post>) : RecyclerView.Adapter<MainAdapter.Holder>() {

    interface ItemClick {
        fun onClick(view: View, position: Int)
    }

    var itemClick : ItemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemMainPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun getData(newData: MutableList<Post>){
        data = newData
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.itemView.setOnClickListener {
            itemClick?.onClick(it, position)
        }
        holder.ivPost.setImageResource(data[position].img)
        holder.tvName.text = data[position].name
        holder.tvLocation.text = data[position].address
        holder.tvPrice.text = buildString {
        append(String.format("%,d", data[position].price))
        append(holder.itemView.context.getString(R.string.all_money))
    }
        holder.tvChat.text = data[position].chat.toString()
        holder.tvLike.text = data[position].like.toString()
    }

    inner class Holder(val binding: ItemMainPostBinding) : RecyclerView.ViewHolder(binding.root) {
        val ivPost = binding.ivMainPost
        val tvName = binding.tvMainName
        val tvLocation = binding.tvMainLocation
        val tvPrice = binding.tvMainPrice
        val ivChat = binding.ivMainChat
        val tvChat = binding.tvMainChat
        val ivLike = binding.ivMainLike
        val tvLike = binding.tvMainLike
    }
}