package com.example.applemarket.main

import android.app.Application
import android.content.Context
import android.icu.text.Transliterator.Position
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.applemarket.Post
import com.example.applemarket.PostData
import com.example.applemarket.PostData.loadList

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val _totalPost: MutableLiveData<MutableList<Post>> = MutableLiveData()

    val totalPost: LiveData<MutableList<Post>> get() = _totalPost
    init {
        val context = getApplication<Application>().applicationContext
        context.loadList(context)
        _totalPost.value = PostData.totalPost
    }

    fun deletePost(position: Int) {
        PostData.totalPost.removeAt(position)
        _totalPost.value = PostData.totalPost
    }
}