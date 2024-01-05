package com.example.applemarket.Detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.applemarket.Post
import com.example.applemarket.PostData

class DetailViewModel (application: Application) : AndroidViewModel(application) {
    private val _postData: MutableLiveData<Post> =  MutableLiveData()
    val postData: LiveData<Post> get() = _postData

    fun loadPost(number: Int) {
        _postData.value = PostData.totalPost.find { it.number == number }
    }
}