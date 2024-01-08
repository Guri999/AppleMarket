package com.example.applemarket.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.applemarket.Post
import com.example.applemarket.PostRepository

class MainViewModel(private val postRepository: PostRepository) : ViewModel() {

    private val _totalPost: MutableLiveData<MutableList<Post>> = MutableLiveData()
    val totalPost: LiveData<MutableList<Post>> get() = _totalPost

    private val _postData: MutableLiveData<Post> = MutableLiveData()
    val postData: LiveData<Post> get() = _postData

    /**
     * Todo 처음 실행시 더미데이터에서 데이터 로드
    */
    init {
        postRepository.loadList()
        _totalPost.value = postRepository.totalPost
    }

    /**
     * TODO 디테일 페이지에서 포스트 갱신
     *
     * @param data totalPost의 요소
     */
    fun setPost(data: Post) {
        _postData.value = data
    }

    fun deletePost(position: Int) {
        postRepository.deletePost(position)
        _totalPost.value = postRepository.totalPost
    }

    fun onClickLike(data: Post) {
        postRepository.onClickLike(data)
        _totalPost.value = postRepository.totalPost
        _postData.value = postRepository.detailPost
    }
}