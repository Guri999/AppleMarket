package com.example.applemarket

import android.content.Context
import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader



class PostRepository(private val context: Context) {

    lateinit var totalPost: MutableList<Post>
    lateinit var detailPost: Post
    private var canLoad = true

    fun loadList() {
        if (canLoad) {
            canLoad = false
            val assetManager = context.assets
            val inputStream = assetManager.open("dummy_data.tsv")
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            bufferedReader.forEachLine {
                val tokens = it.split("\t")
                val resource =
                    context.resources.getIdentifier(tokens[1], "drawable", context.packageName)
                val post = Post(
                    tokens[0].toInt(),
                    resource,
                    tokens[2],
                    tokens[3].replace("\\n", "\n").replace(" + ", "").replace("\"", ""),
                    tokens[4],
                    tokens[5].toInt(),
                    tokens[6],
                    tokens[7].toInt(),
                    tokens[8].toInt()
                )
                PostData.totalPost.add(post)
                totalPost = PostData.totalPost
            }
        }
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
            totalPost = PostData.totalPost
        }
    }

}

