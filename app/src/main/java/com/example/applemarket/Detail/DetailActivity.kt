package com.example.applemarket.Detail

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
        ViewModelProvider(this).get(DetailViewModel::class.java)
    }
    private val postData by lazy {
        intent.getParcelableExtra<Post>("data")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        init()
    }

    private fun init(){
        setInfo()

        setBackBtn()
    }

    /**
     * TODO
     *  파셀라이즈 데이터를 받고
     *
     *  그걸 바탕으로 액티비티에 정보를 갱신
     */
    private fun setInfo() {
        postData!!.also { data ->
            binding.also {
                it.ivDetailImg.setImageResource(data.img)
                it.tvDetailSeller.text = data.user
                it.tvDetailAddress.text = data.address
                it.tvDetailPostname.text = data.name
                it.tvDetailPrice.text = buildString {
        append(String.format("%,d", data.price))
        append(getString(R.string.all_money))
    }
                it.tvDetailDetail.text = data.detail
            }
        }
    }

    private fun setBackBtn(){
        binding.ivDetailBack.setOnClickListener {
            finish()
        }
    }
}