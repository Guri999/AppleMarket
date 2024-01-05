package com.example.applemarket

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
    val like: Int,
    val chat: Int
) : Parcelable

object PostData {
    var totalPost: MutableList<Post> = arrayListOf()

    fun Context.loadList(context: Context) {
        val assetManager = context.assets
        val inputStream = assetManager.open("dummy_data.tsv")
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        bufferedReader.forEachLine {
            val tokens = it.split("\t")
            val resource = context.resources.getIdentifier(tokens[1],"drawable", context.packageName)
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