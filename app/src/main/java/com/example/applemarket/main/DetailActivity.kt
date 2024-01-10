package com.example.applemarket.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.applemarket.InteractionMessage
import com.example.applemarket.Post
import com.example.applemarket.PostRepositoryImpl
import com.example.applemarket.R
import com.example.applemarket.databinding.ActivityDetailBinding
import com.google.android.material.snackbar.Snackbar

@Suppress("DEPRECATION")
class DetailActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityDetailBinding.inflate(layoutInflater)
    }
    private val postRepository by lazy {
        PostRepositoryImpl()
    }
    private val viewModel by lazy {
        ViewModelProvider(this, MainViewModelFactory(postRepository))[MainViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        intent.getParcelableExtra<Post>("data")?.let { viewModel.setPost(it) }
        viewModel.postData.observe(this) {
            setInfo(it)
        }

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
                if (data.userlike) it.ivDetailLike.setImageResource(R.drawable.img_all_like)
                else it.ivDetailLike.setImageResource(R.drawable.img_all_emptylike)
                it.tvDetailDetail.text = data.detail
            }
        }
        setLikeBtn(post)
    }

    /**
     * TODO 스낵바 출력 구현
     *
     * 맨처음 눌렀을때 좋아요 이미지가 빈하트면 스낵바가 안뜨고
     * 하트이미지가 변경되긴하는데 다시눌러서 빈하트를 만들고 또 다시 눌러야 스낵바가 뜸
     * 맨처음에 이미 좋아요가 눌린상태면 눌렀을때 빈하트가 되면서 바로 스낵바가 뜬다
     * 그다음은 다시 하트 이미지가 변경되었을때 스낵바가 뜨게됨
     *
     * 이런 버그가 발생한 원인이 뭘까?
     * 그건 뷰모델과 관련있는데 뷰모델과 액티비티는 서로 독립적으로 진행되기 때문에
     * 뷰모델에선 계산하는데 스낵바로직이 바로 실행됬기 때문인걸로 추정된다
     */
    private fun setLikeBtn(post: Post) {
        binding.ivDetailLike.setOnClickListener {
            if (post.userlike.not()) Snackbar.make(
                binding.tvDetailPrice,
                InteractionMessage.SNACKPLUS.message,
                Snackbar.LENGTH_LONG
            ).show()
            viewModel.onClickLike(post)
        }
    }

    private fun setBackBtn() {
        binding.ivDetailBack.setOnClickListener {
            finish()
        }
    }
}