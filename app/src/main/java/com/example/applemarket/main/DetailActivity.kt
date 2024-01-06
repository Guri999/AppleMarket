package com.example.applemarket.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.applemarket.Post
import com.example.applemarket.R
import com.example.applemarket.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityDetailBinding.inflate(layoutInflater)
    }
    private val viewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        init()
    }

    private fun init(){
        intent.getParcelableExtra<Post>("data")?.let { viewModel.setPost(it) }
        viewModel.postData.observe(this, Observer {
            setInfo(it)
        })

        setBackBtn()
    }

    /**
     * TODO
     *  파셀라이즈 데이터를 라이브 데이터로 받는다
     *
     *  그걸 바탕으로 액티비티에 정보를 갱신
     */
    private fun setInfo(post: Post) {
        post.also { data ->
            binding.also {
                it.ivDetailImg.setImageResource(data.img)
                it.tvDetailSeller.text = data.user
                it.tvDetailAddress.text = data.address
                it.tvDetailPostname.text = data.name
                it.tvDetailPrice.text = buildString {
        append(String.format("%,d", data.price))
        append(getString(R.string.all_money))
    }
                if (data.userlike == 0) it.ivDetailLike.setImageResource(R.drawable.img_all_emptylike)
                else it.ivDetailLike.setImageResource(R.drawable.img_all_like)
                it.tvDetailDetail.text = data.detail
            }
        }
        setLikeBtn(post)
    }

    private fun setLikeBtn(post: Post){
        binding.ivDetailLike.setOnClickListener {
            viewModel.onClickLike(post)
        }
    }
    private fun setBackBtn(){
        binding.ivDetailBack.setOnClickListener {
            finish()
        }
    }
}