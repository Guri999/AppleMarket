package com.example.applemarket.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.applemarket.Post
import com.example.applemarket.PostRepository
import com.example.applemarket.PostRepositoryImpl

class MainViewModel(private val postRepository: PostRepository) : ViewModel() {

    private val _totalPost: MutableLiveData<MutableList<Post>> = MutableLiveData()
    val totalPost: LiveData<MutableList<Post>> get() = _totalPost

    private val _postData: MutableLiveData<Post> = MutableLiveData()
    val postData: LiveData<Post> get() = _postData

    fun setList() {
        postRepository.setList()
        _totalPost.value = postRepository.setList()
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
        _totalPost.value = postRepository.setList()
    }

    fun onClickLike(data: Post) {
        postRepository.onClickLike(data)
        _totalPost.value = postRepository.setList()
        _postData.value = postRepository.onClickLike(data)
    }
}