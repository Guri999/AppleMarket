package com.example.applemarket

import android.content.Context
import android.util.Log


class PostRepository {

    var totalPost: MutableList<Post> = mutableListOf()
    var detailPost: Post? = null

    fun setList(){
        totalPost = PostData.totalPost
    }

    /**
     * TODO 데이터에서 특정 포스트 제거
     *
     * @param position 포스트 위치
     */
    fun deletePost(position: Int) {
        PostData.totalPost.removeAt(position)
        totalPost = PostData.totalPost
    }
    /**
     * TODO 좋아요 버튼 클릭 액션
     *
     * @param data totalPost에서 출력되는 포스트
     * totalPost에서 data와 같은 요소를 찾고 값을 변경해줌
     * 라이브 데이터로 옵저빙중이라 값이 바뀌면 액티비티에서 자동으로 갱신된다
     */
    fun onClickLike(data: Post) {
        PostData.totalPost.find { it == data }?.let { post ->
            if (data.userlike) {
                post.userlike = false
                post.like--
            } else {
                post.userlike = true
                post.like++
            }
            detailPost = post
        }
        totalPost = PostData.totalPost
    }

}

