package com.example.applemarket.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.applemarket.Post
import com.example.applemarket.R
import com.example.applemarket.databinding.ItemMainPostBinding

class MainAdapter(private var data: MutableList<Post>,private val viewModel: MainViewModel) : RecyclerView.Adapter<MainAdapter.Holder>() {

    interface ItemClick {
        fun onClick(view: View, position: Int)

    }

    var itemClick : ItemClick? = null

    interface ItemLongClick{

        fun onLongClick(view: View, position: Int)
    }

    var itemLongClick : ItemLongClick? = null
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

    /**
     * TODO 데이터 갱신
     *
     * @param newData 라이브 데이터는 실시간으로 totalPost가 변하는지 관찰중 임으로
     *
     * 변화가 있다면 데이터를 갱신할것이다
     */
    @SuppressLint("NotifyDataSetChanged")
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
        //좋아요 표시하는 데이터 처리는 뷰모델에서 처리
        holder.ivLike.setImageResource(viewModel.setLike(position))

        onDeletePost(holder,position)
    }

    /**
     * TODO 롱클릭 리스너를 사용해서 다이얼로그를 출력
     *
     * @param holder 리사이클러뷰 각 테이블의 위젯
     * @param position 위치
     */
    private fun onDeletePost(holder: Holder, position: Int) {
        holder.itemView.setOnLongClickListener {
            itemLongClick?.onLongClick(it, position)
            true
        }
    }
    inner class Holder(private val binding: ItemMainPostBinding) : RecyclerView.ViewHolder(binding.root) {
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