package com.example.applemarket

import android.annotation.SuppressLint
import android.content.Context
import android.os.Parcelable
import com.example.applemarket.Post
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlinx.parcelize.Parcelize
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.io.path.Path

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
 * 좋아요 추가
 *
 * 데이터값을 변경한다
 * 디폴트값을 주고 좋아요를 누르면 값이 변경되게 한뒤 구분
 * 데이터의 값이 변하기때문에 뷰모델에서 자동적으로 라이브데이터가 옵저빙할것이고
 * UI가 자동적으로 업데이트 될테니 이걸로 채결
 */

object PostData {
    var totalPost: MutableList<Post> = arrayListOf()
    var locate = listOf("내배캠동", "내배캠서", "내배캠남", "내배캠북", "내배캠시", "내배캠구", "내배캠군", "내배캠쨩")
}

