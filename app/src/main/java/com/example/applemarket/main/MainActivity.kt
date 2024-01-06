package com.example.applemarket.main

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

    /**
     * 매인 액티비티가 다시 활성화 될때 데이터 갱신
     *
     * 생명주기가 onResume일때
     * 즉 사용자와 상호작용하기 전에 데이터 값을 갱신해준다
     */
    override fun onResume() {
        super.onResume()
        viewModel.setData()
    }

    private fun init() {
        setPost()

        setNoticebtn()

        setFab()
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
     * TODO fab 스크롤 이벤트 추가
     *
     * 리사이클러뷰 관련해서 엄청 찾아봤는데
     * 생각해보니 액션바 때문에 네스티드뷰로 감싸서 리사이클러뷰를 드래그하는게 아니었다
     * 네스티드뷰에 스크롤리스너를 달아서 해결
     * 생각해보니 네스티드뷰를 사용하면 리사이클러뷰의 장점을 죽이는 것 같다
     * 리사이클러뷰와 fab appbar, 과제에 아주 적합한 레이아웃인 코디네이터 레이아웃으로 바꾸고
     * 다시 리사이클러뷰 관련 코드로 변경
     */
    private fun setFab() {
        binding.rcMainPost.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(-1)) {
                    binding.faMainFab.animate().alpha(0f).setDuration(300).withEndAction {
                        binding.faMainFab.visibility = View.GONE
                    }
                } else {
                    binding.faMainFab.visibility = View.VISIBLE
                    binding.faMainFab.animate().alpha(1f).setDuration(300)
                }
            }
        })

        onClickFab()
    }

    private fun onClickFab() {
        binding.faMainFab.setOnClickListener {
            binding.rcMainPost.scrollToPosition(0)
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

    /**
     * 포스트 삭제 다이얼로그
     *
     * 기본 다이얼로그로 제작
     */
    private fun setDelDialog(position: Int) {
        AlertDialog.Builder(this)
            .setIcon(R.drawable.img_main_chat_16dp)
            .setTitle(getString(R.string.dialog_del_product))
            .setMessage(getString(R.string.dialog_del_again))
            .setPositiveButton(getString(R.string.common_confirm)) { _, _ ->
                viewModel.deletePost(position)
            }
            .setNegativeButton(getString(R.string.common_cancel), null)
            .show()
    }

    /**
     * TODO
     * 안드로이드 백 버튼 클릭시 다이얼로그
     *
     * 커스텀 다이얼로그로 제작, 기본과 유사하지만 볼드체로 되어있음
     */
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setView(layoutInflater.inflate(R.layout.main_dialog, null))
            .create()
            .let { dialog ->
                dialog.setOnShowListener {
                    val confirm = dialog.findViewById<TextView>(R.id.tv_dia_confirm)
                    val cancel = dialog.findViewById<TextView>(R.id.tv_dia_cancel)

                    confirm?.setOnClickListener {
                        dialog.dismiss()
                        super.onBackPressed()
                    }
                    cancel?.setOnClickListener {
                        dialog.dismiss()
                    }
                }
                dialog.show()
            }
    }

    /**
     * TODO
     * 알림 만들고 보내기
     */
    private fun setNoticebtn() {
        binding.ivMainNotice.setOnClickListener {
            setNotice()
        }
    }

    private fun setNotice() {
        noticeManager.createNotificationChannel(
            NotificationChannel(
                "main-channel",
                getString(R.string.notice_keyword),
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = getString(R.string.notice_keyword_info)
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
            setContentTitle(getString(R.string.notice_keyword))
            setContentText(getString(R.string.notice_keyword_message))
        }

        noticeManager.notify(1, noticeBuilder.build())
    }
}