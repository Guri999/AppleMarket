package com.example.applemarket.main

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.applemarket.Detail.DetailActivity
import com.example.applemarket.PostData
import com.example.applemarket.PostData.totalPost
import com.example.applemarket.R
import com.example.applemarket.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val viewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }
    private val adapter by lazy { MainAdapter(mutableListOf(), viewModel) }

    private val noticeBuilder by lazy {
        NotificationCompat.Builder(this, "main-channel")
    }

    private val noticeManager by lazy { getSystemService(NOTIFICATION_SERVICE) as NotificationManager }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        setPost()

        setNoticebtn()
    }

    /**
     * TODO
     * 포스트 데이터 세팅
     *
     * 리사이클러 뷰의 레이아웃 매니저를 리니어 매니저로 설정
     *
     * 뷰모델에서 포스트들의 정보를 totalPost에 라이브 데이터로 갱신
     *
     * 갱신된 데이터를 옵저빙해서 어뎁터에 넘겨준다
     */
    private fun setPost() {
        binding.rcMainPost.layoutManager = LinearLayoutManager(this@MainActivity)
        binding.rcMainPost.adapter = adapter
        viewModel.totalPost.observe(this) { post ->
            adapter.getData(post)
        }

        setDetailBtn()
        setDeleteBtn()
    }

    /**
     * TODO
     *  DetailPage로 이동하고 포스트 정보 넘기기
     */
    private fun setDetailBtn() {
        adapter.itemClick = object : MainAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra("data", totalPost[position])
                startActivity(intent)
            }
        }
    }

    /**
     * TODO 롱클릭시 액션 설정
     *
     * 다이얼로그 출력
     */
    private fun setDeleteBtn() {
        adapter.itemLongClick = object : MainAdapter.ItemLongClick {
            override fun onLongClick(view: View, position: Int) {
                setDelDialog(position)
            }
        }
    }
    private fun setDelDialog(position:Int) {
        AlertDialog.Builder(this)
            .setIcon(R.drawable.img_main_chat_16dp)
            .setTitle("상품 삭제")
            .setMessage("상품을 정말로 삭제하시겠습니까?")
            .setPositiveButton("확인") { _, _ ->
                viewModel.deletePost(position)
            }
            .setNegativeButton("취소", null)
            .show()
    }
    /**
     * TODO
     * 안드로이드 백 버튼 클릭시 다이얼로그
     */
    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setIcon(R.drawable.img_main_chat_16dp)
            .setTitle("종료")
            .setMessage("정말 종료하시겠습니까?")
            .setPositiveButton("확인") { _, _ ->
                super.onBackPressed()
            }
            .setNegativeButton("취소", null)
            .show()
    }

    /**
     * TODO
     * 알림 만들고 보내기
     */
    private fun setNoticebtn() {
        binding.ivMainNotice.setOnClickListener {
            Notice()
        }
    }

    private fun Notice() {
        noticeManager.createNotificationChannel(
            NotificationChannel(
                "main-channel",
                "키워드 알림",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "선택한 키워드 물건 알림"
                /**
                setSound(
                    RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setUsage(AudioAttributes.USAGE_ALARM).build()
                )*/
                enableVibration(true)
            }
        )

        noticeBuilder.run {
            setSmallIcon(R.drawable.img_notice_icon)
            setWhen(System.currentTimeMillis())
            setContentTitle("키워드 알림")
            setContentText("선택한 키워드에 대한 알림이 도착햇습니다!")
        }

        noticeManager.notify(1, noticeBuilder.build())
    }
}