package com.example.applemarket

import android.app.Application
import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        loadList()
        PostRepository().setList()
    }

    private fun loadList() {
        val assetManager = assets
        val inputStream = assetManager.open("dummy_data.tsv")
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        bufferedReader.forEachLine {
            val tokens = it.split("\t")
            val resource = resources.getIdentifier(tokens[1], "drawable", packageName)
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