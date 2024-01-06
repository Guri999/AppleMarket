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
import com.example.applemarket.R

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val _totalPost: MutableLiveData<MutableList<Post>> = MutableLiveData()
    val totalPost: LiveData<MutableList<Post>> get() = _totalPost

    private val _postData: MutableLiveData<Post> = MutableLiveData()
    val postData: LiveData<Post> get() = _postData

    init {
        val context = getApplication<Application>().applicationContext
        context.loadList(context)
        setData()
    }
    fun setData(){
        _totalPost.value = PostData.totalPost
    }

    /**
     * 데이터에서 특정 포스트 제거
     *
     * @param position 포스트 위치
     */
    fun deletePost(position: Int) {
        PostData.totalPost.removeAt(position)
        setData()
    }

    /**
     * 좋아요 이미지 반환
     *
     * @param position 포스트 위치
     * @return userlike데이터 확인후 이미지 리소스값 반환
     */
    fun setLike(position: Int): Int{
        return if (PostData.totalPost[position].userlike == 1) R.drawable.img_all_like
        else R.drawable.img_all_emptylike
    }

    /**
     * 디테일 페이지에서 포스트 갱신
     *
     * @param data totalPost의 요소
     */
    fun setPost(data: Post) {
        _postData.value = data
    }

    /**
     * 좋아요 버튼 클릭 액션
     *
     * @param data totalPost에서 출력되는 포스트
     * totalPost에서 data와 같은 요소를 찾고 값을 변경해줌
     * 라이브 데이터로 옵저빙중이라 값이 바뀌면 액티비티에서 자동으로 갱신된다
     */
    fun onClickLike(data: Post) {
        PostData.totalPost.find { it == data }?.let {
            if (data.userlike == 0) {
                it.userlike = 1
                it.like++
            } else {
                PostData.totalPost.find { it == data }?.let {
                    it.userlike = 0
                    it.like--
                }
            }
            _postData.value = it
        }
    }
}