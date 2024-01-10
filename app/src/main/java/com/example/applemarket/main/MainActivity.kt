package com.example.applemarket.main

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.applemarket.InteractionMessage
import com.example.applemarket.PostData
import com.example.applemarket.PostRepositoryImpl
import com.example.applemarket.R
import com.example.applemarket.databinding.ActivityMainBinding

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val postRepository by lazy {
        PostRepositoryImpl()
    }
    private val viewModel by lazy {
        ViewModelProvider(this, MainViewModelFactory(postRepository))[MainViewModel::class.java]
    }
    private val adapter by lazy { MainAdapter(mutableListOf()) }

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
     * TODO 매인 액티비티가 다시 활성화 될때 데이터 갱신
     *
     * 생명주기가 onResume일때
     * 즉 사용자와 상호작용하기 전에 데이터 값을 갱신해준다
     */
    override fun onResume() {
        super.onResume()
        setPost()
    }

    private fun init() {
        setPost()

        setNoticebtn()

        setFab()

        setLocate()
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
        viewModel.setList()
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
                intent.putExtra("data", viewModel.totalPost.value?.get(position))
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
                    binding.faMainFab.animate().alpha(1f).duration = 300
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
     * TODO 포스트 삭제 버튼 - 다이얼로그에 데이터 보내기
     *
     * 다이얼로그를 재사용하기위해 그냥 만드는게 아닌 데이터를 전달해서 제작하게함
     *
     * 타이틀과 메세지를 채워줄 text를 보내고, 확인버튼과 취소버튼의 로직을 람다표현식으로 인자로 바꿔서 setCustomDialog를 호출
     */
    private fun setDeleteBtn() {
        adapter.itemLongClick = object : MainAdapter.ItemLongClick {
            override fun onLongClick(view: View, position: Int) {
                setCustomDialog(InteractionMessage.DELETEPRODUCT, InteractionMessage.DELETEMESSAGE,
                    { dialog -> dialog.dismiss(); viewModel.deletePost(position) },
                    { dialog -> dialog.dismiss() })
            }
        }
    }

    /**
     * TODO 뒤로가기시 다이얼로그 호출
     */
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        setCustomDialog(InteractionMessage.END, InteractionMessage.ENDMESSAGE,
            { dialog -> dialog.dismiss(); super.onBackPressed() },
            { dialog -> dialog.dismiss() })
    }

    /**
     * TODO 다이얼로그 세팅후 출력
     */
    @SuppressLint("InflateParams")
    private fun setCustomDialog(title: InteractionMessage, message: InteractionMessage, confirmAction: (AlertDialog) -> Unit, cancelAction: (AlertDialog) -> Unit) {
        val dialog = AlertDialog.Builder(this)
            .setView(layoutInflater.inflate(R.layout.main_dialog, null))
            .create()

        dialog.setOnShowListener {
            setupDialog(dialog, title, message, confirmAction, cancelAction)
        }
        dialog.show()
    }
    private fun setupDialog(dialog: AlertDialog, title: InteractionMessage, message: InteractionMessage, confirmAction: (AlertDialog) -> Unit, cancelAction: (AlertDialog) -> Unit) {
        dialog.findViewById<TextView>(R.id.tv_dia_title)?.setText(title.message)
        dialog.findViewById<TextView>(R.id.tv_dia_message)?.setText(message.message)
        dialog.findViewById<TextView>(R.id.bt_dia_confirm)?.setOnClickListener { confirmAction(dialog) }
        dialog.findViewById<TextView>(R.id.bt_dia_cancel)?.setOnClickListener { cancelAction(dialog) }
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
                getString(InteractionMessage.NOTICEKEYWORD.message),
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = getString(InteractionMessage.NOTICEKEYWORDINFO.message)
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
            setContentTitle(getString(InteractionMessage.NOTICEKEYWORD.message))
            setContentText(getString(InteractionMessage.NOTICEKEYWORDMESSAGE.message))
        }

        noticeManager.notify(1, noticeBuilder.build())
    }

    private fun setLocate(){
        binding.spMainSpinner.adapter = MainSpinnerAdapter(this, R.layout.item_main_spinner, PostData.locate)
        binding.spMainSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected( parent: AdapterView<*>?, view: View?, position: Int,id: Long) {
                binding.spMainSpinner.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }
}