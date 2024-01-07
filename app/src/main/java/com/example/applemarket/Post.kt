package com.example.applemarket

import android.annotation.SuppressLint
import android.content.Context
import android.os.Parcelable
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlinx.parcelize.Parcelize

@Parcelize
data class Post(
    val number: Int?,
    val img: Int,
    val name: String,
    val detail: String,
    val user: String,
    val price: Int,
    val address: String,
    var like: Int,
    var chat: Int,
    var userlike: Boolean = false
) : Parcelable

/**
 * 좋아요 추가 두번째 방법
 *
 * 데이터값을 변경한다
 * 디폴트값을 주고 좋아요를 누르면 값이 변경되게 한뒤 구분
 * 데이터의 값이 변하기때문에 뷰모델에서 자동적으로 라이브데이터가 옵저빙할것이고
 * UI가 자동적으로 업데이트 될테니 이걸로 채결
 */

object PostData {
    var totalPost: MutableList<Post> = arrayListOf()
    var canLoad = true
    @SuppressLint("DiscouragedApi")
    fun Context.loadList(context: Context) {
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
            }
        }
    }

    /**
     * 좋아요 추가 첫번째 방법
     * 포스트의 수만큼의 크기를 가진 리스트를 만들고 안의 숫자를 0으로 초기화
     * 좋아요를 누르면 Post의 인덱스값 혹은 넘버를 배열의 index값으로 받아 1을 증가시킨다
     * 그 다음 데이터를 갱신, 1을가진 요소와 인덱스값이 같은 포스터가 있다면 EntryType을 Like로 변경하고 좋아요가 눌린채로 등장하게 한다
     */

//    var userLike = MutableList<Int>(totalPost.size) { 0 }

    var locate = listOf("내배캠동","내배캠서","내배캠남","내배캠북","내배캠시","내배캠구","내배캠군","내배캠쨩")
}