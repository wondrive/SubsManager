package com.example.subsmanager2.board

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.subsmanager2.R
import com.example.subsmanager2.dao.PlatformBoardDao
import com.example.subsmanager2.database.DatabaseModule
import com.example.subsmanager2.entity.BoardEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firestore.*
import kotlinx.android.synthetic.main.fragment_board_write.*
import kotlinx.android.synthetic.main.fragment_board_write.view.*
import kotlinx.android.synthetic.main.fragment_board_write.view.btn_save
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class platformBoardWriteFragment : DialogFragment() {

    /* note 객체 생성 및 초기화. */
    private var board = BoardEntity(boardContent = "", boardTitle = "",userId = "",subFee = "",usage = "",subContents = "",boardCreateDt = "")
    val boardDao = PlatformBoardDao()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        /* 추가/수정 다이얼로그 위한 레이아웃(dialog_note_create.xml) inflate */
        val rootView = inflater.inflate(R.layout.fragment_platform_board_write, container, false)
        return rootView
    }//end of onCreateView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

// 아마 나중에 삭제될 코드
//        val updateYn = arguments?.getString("UPDATE_YN") ?: "N"
//        if(updateYn.equals("Y")) {
//            val boardId = arguments?.getLong("BOARD_ID") ?: kotlin.run { throw Error("BOARD_ID가 없습니다.") }
//            viewLifecycleOwner.lifecycleScope.launch {
//                /* 미리 변수를 선언 */
//                var savedBoard: BoardEntity? = null
//                /* 노트를 쿼리.*/
//                withContext(Dispatchers.IO) {
//                    savedBoard = dao.selectBoard(boardId)
//                }
//                /* 노트가 존재한다면 note를 변경*/
//                savedBoard?.let {
//                    board = it //쿼리한 노트 객체를 note에 저장
////                    view.txt_title.setText(it.boardTitle)
//                    view.txt_content.setText(it.boardContent)
//                }
//            }
//        }//end of let

        //별점 값
//        lateinit var ratingBar: RatingBar

        //태그 선택
        var selectFee : Boolean = false
        var selectContents: Boolean =false
        var selectUsage : Boolean =false

        // 선택된 태그 값
        var subappName : String=""
        var fee :String ="" // 가격
        var contents: String="테스트 contemts" // 컨텐츠
        var usage: String="테스트 usage" // 지속 사용

        // TODO: 중복덕지코드~!! 수정해야함 !
        lateinit var btn_fee1: Button
        lateinit var btn_fee2: Button
        lateinit var btn_fee3: Button
        btn_fee1 = view.btn_fee1
        btn_fee2 = view.btn_fee2
        btn_fee3 = view.btn_fee3

        lateinit var btn_contents1: Button
        lateinit var btn_contents2: Button
        lateinit var btn_contents3: Button
        btn_contents1 = view.btn_contents1
        btn_contents2 = view.btn_contents2
        btn_contents3 = view.btn_contents3

        lateinit var btn_usage1: Button
        lateinit var btn_usage2: Button
        btn_usage1 = view.btn_usage1
        btn_usage2 = view.btn_usage2


        fun selectFeeBtn(btn_fee1: Button, btn_fee2: Button, btn_fee3: Button){
            btn_fee1.setOnTouchListener(object : View.OnTouchListener {
                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                    when (event?.action) {
                        MotionEvent.ACTION_DOWN -> {
                            if(selectFee) {
                                btn_fee2.setBackgroundResource(R.drawable.button_review)
                                btn_fee3.setBackgroundResource(R.drawable.button_review)

                                btn_fee1.setBackgroundResource(R.drawable.button_review_click)
                                fee= btn_fee1.text as String
                                selectFee = !selectFee
                            }
                            else{
                                selectFee = !selectFee
                                btn_fee1.setBackgroundResource(R.drawable.button_review)
                            }
                        }
                    }
                    //리턴값이 false면 seekbar 동작 안됨
                    return true //or false
                }
            })

            btn_fee2.setOnTouchListener(object : View.OnTouchListener {
                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                    when (event?.action) {
                        MotionEvent.ACTION_DOWN -> {
                            if(selectFee) {
                                btn_fee1.setBackgroundResource(R.drawable.button_review)
                                btn_fee3.setBackgroundResource(R.drawable.button_review)

                                btn_fee2.setBackgroundResource(R.drawable.button_review_click)
                                //선택 값
                                fee= btn_fee2.text as String
                                selectFee = !selectFee
                            }
                            else{
                                selectFee = !selectFee
                                btn_fee2.setBackgroundResource(R.drawable.button_review)
                            }
                        }
                    }
                    //리턴값이 false면 seekbar 동작 안됨
                    return true //or false
                }
            })

            btn_fee3.setOnTouchListener(object : View.OnTouchListener {
                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                    when (event?.action) {
                        MotionEvent.ACTION_DOWN -> {
                            if(selectFee) {
                                btn_fee1.setBackgroundResource(R.drawable.button_review)
                                btn_fee2.setBackgroundResource(R.drawable.button_review)

                                btn_fee3.setBackgroundResource(R.drawable.button_review_click)
                                //선택 값
                                fee= btn_fee3.text as String
                                selectFee = !selectFee
                            }
                            else{
                                selectFee = !selectFee
                                btn_fee3.setBackgroundResource(R.drawable.button_review)
                            }
                        }
                    }
                    //리턴값이 false면 seekbar 동작 안됨
                    return true //or false
                }
            })
        }
        fun selectContentsBtn(btn_contents1: Button, btn_contents2: Button, btn_contents3: Button){
            btn_contents1.setOnTouchListener(object : View.OnTouchListener {
                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                    when (event?.action) {
                        MotionEvent.ACTION_DOWN -> {
                            if(selectContents) {
                                btn_contents2.setBackgroundResource(R.drawable.button_review)
                                btn_contents3.setBackgroundResource(R.drawable.button_review)

                                btn_contents1.setBackgroundResource(R.drawable.button_review_click)
                                contents= btn_contents1.text as String
                                selectContents = !selectContents
                            }
                            else{
                                selectContents = !selectContents
                                btn_contents1.setBackgroundResource(R.drawable.button_review)
                            }
                        }
                    }
                    //리턴값이 false면 seekbar 동작 안됨
                    return true //or false
                }
            })

            btn_contents2.setOnTouchListener(object : View.OnTouchListener {
                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                    when (event?.action) {
                        MotionEvent.ACTION_DOWN -> {
                            if(selectContents) {
                                btn_contents1.setBackgroundResource(R.drawable.button_review)
                                btn_contents3.setBackgroundResource(R.drawable.button_review)

                                btn_contents2.setBackgroundResource(R.drawable.button_review_click)
                                //선택 값
                                contents= btn_contents2.text as String
                                selectContents = !selectContents
                            }
                            else{
                                selectContents = !selectContents
                                btn_contents2.setBackgroundResource(R.drawable.button_review)
                            }
                        }
                    }
                    //리턴값이 false면 seekbar 동작 안됨
                    return true //or false
                }
            })

            btn_contents3.setOnTouchListener(object : View.OnTouchListener {
                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                    when (event?.action) {
                        MotionEvent.ACTION_DOWN -> {
                            if(selectContents) {
                                btn_contents1.setBackgroundResource(R.drawable.button_review)
                                btn_contents2.setBackgroundResource(R.drawable.button_review)

                                btn_contents3.setBackgroundResource(R.drawable.button_review_click)
                                //선택 값
                                contents= btn_contents3.text as String
                                selectContents = !selectContents
                            }
                            else{
                                selectContents = !selectContents
                                btn_contents3.setBackgroundResource(R.drawable.button_review)
                            }
                        }
                    }
                    //리턴값이 false면 seekbar 동작 안됨
                    return true //or false
                }
            })
        }
        fun selectUsageBtn(btn_usage1: Button, btn_usage2: Button){
            btn_usage1.setOnTouchListener(object : View.OnTouchListener {
                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                    when (event?.action) {
                        MotionEvent.ACTION_DOWN -> {
                            if(selectUsage) {
                                btn_usage2.setBackgroundResource(R.drawable.button_review)
                                btn_usage1.setBackgroundResource(R.drawable.button_review_click)
                                usage= btn_usage1.text as String
                                selectUsage = !selectUsage
                            }
                            else{
                                selectUsage = !selectUsage
                                btn_usage1.setBackgroundResource(R.drawable.button_review)
                            }
                        }
                    }
                    //리턴값이 false면 seekbar 동작 안됨
                    return true //or false
                }
            })

            btn_usage2.setOnTouchListener(object : View.OnTouchListener {
                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                    when (event?.action) {
                        MotionEvent.ACTION_DOWN -> {
                            if(selectUsage) {
                                btn_usage1.setBackgroundResource(R.drawable.button_review)
                                btn_usage2.setBackgroundResource(R.drawable.button_review_click)
                                //선택 값
                                usage= btn_usage2.text as String
                                selectUsage = !selectUsage
                            }
                            else{
                                selectUsage = !selectUsage
                                btn_usage2.setBackgroundResource(R.drawable.button_review)
                            }
                        }
                    }
                    //리턴값이 false면 seekbar 동작 안됨
                    return true //or false
                }
            })
        }

        selectFeeBtn(btn_fee1,btn_fee2,btn_fee3)
        selectContentsBtn(btn_contents1,btn_contents2,btn_contents3)
        selectUsageBtn(btn_usage1,btn_usage2)

        //TODO: 필수항목 입력시 작성하기 버튼이 활성화 됨
        if(selectContents && selectFee && selectUsage){
            btn_save.setBackgroundColor(R.drawable.button_review_click)
        }

        //게시판 작성날짜 생성
        fun setCreateDt(): String{
            val cal = Calendar.getInstance()
            cal.time = Date()
            val df: DateFormat = SimpleDateFormat("yyyy-MM-dd")
            return df.format(cal.time)
        }


        //작성하기 버튼을 눌렀을때
        view.btn_save.setOnClickListener {
            // TODO: star rating

//            val msg = ratingBar.rating.toString()
//            Toast.makeText(this.context, "Rating is: "+msg, Toast.LENGTH_SHORT).show()

            //후기에 등록할 구독 앱
            val spinner_subapp_list : Spinner = view.spinner_subapp_list
            subappName = spinner_subapp_list.selectedItem.toString()

            board.boardTitle = subappName
            board.boardContent = view.txt_content.text.toString()
            board.subFee = fee
            board.subContents=contents
            board.usage=usage

            /* 필수항목 작성 검증 */
            if (selectContents || selectFee || selectUsage) {
                Toast.makeText(requireContext(), "필수항목을 작성해주세요!", Toast.LENGTH_LONG).show()
            } else {

                /*
                * TODO: 한 userid는 중복된 플랫폼 리뷰를 쓸 수 없음
                 */
                var userId: String? = ""
                FirebaseAuth.getInstance().currentUser?.let {
                    userId = it.email
                }

                /* 자동 스코프에 맞추어 코루틴을 실행*/
                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                    val board = BoardEntity(
                        boardContent = board.boardContent,
                        boardTitle = board.boardTitle,
                        subFee = board.subFee,
                        usage = board.usage,
                        subContents = board.subContents,
                        userId = userId.toString(),
                        boardCreateDt = setCreateDt()
                    )
                    boardDao.writeBoard(data = board)
                }
            }
            findNavController().popBackStack()
        }
    }//end of view.btn_save.setOnClickListener
}//end of onViewCreated

