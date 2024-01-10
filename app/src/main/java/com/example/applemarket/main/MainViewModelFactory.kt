package com.example.applemarket.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.applemarket.PostRepositoryImpl
import java.lang.IllegalArgumentException

class MainViewModelFactory(private val postRepository: PostRepositoryImpl) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)){
            return MainViewModel(postRepository) as T
        }
        throw IllegalArgumentException("unKnown ViewModel class")
    }
}