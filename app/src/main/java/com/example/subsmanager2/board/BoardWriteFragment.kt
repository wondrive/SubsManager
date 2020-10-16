package com.example.subsmanager2.board

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.subsmanager2.R
import com.example.subsmanager2.database.DatabaseModule
import com.example.subsmanager2.entity.BoardEntity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_board_write.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A simple [Fragment] subclass.
 */
class BoardWriteFragment : DialogFragment() {

    /* note 객체 생성 및 초기화. */
    private var board = BoardEntity(boardContent = "", boardTitle = "",userId = "",subFee = "",usage = "",subContents = "",boardImg = "")

        //noteDao 참조
    private val dao by lazy { DatabaseModule.getDatabase(requireContext()).boardDao() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        /* 추가/수정 다이얼로그 위한 레이아웃(dialog_note_create.xml) inflate */
        val rootView = inflater.inflate(R.layout.fragment_board_write, container, false)


        return rootView
    }//end of onCreateView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //tag?.toLongOrNull()?.let { boardId ->
        val updateYn = arguments?.getString("UPDATE_YN") ?: "N"
        if(updateYn.equals("Y")) {
            val boardId = arguments?.getLong("BOARD_ID") ?: kotlin.run { throw Error("BOARD_ID가 없습니다.") }
            viewLifecycleOwner.lifecycleScope.launch {
                /* 미리 변수를 선언 */
                var savedBoard: BoardEntity? = null
                /* 노트를 쿼리.*/
                withContext(Dispatchers.IO) {
                    savedBoard = dao.selectBoard(boardId)
                }
                /* 노트가 존재한다면 note를 변경*/

                savedBoard?.let {
                    board = it //쿼리한 노트 객체를 note에 저장
//                    view.txt_title.setText(it.boardTitle)
                    view.txt_content.setText(it.boardContent)
                }
            }

        }//end of let

        //태그 선택
        var selectFee : Boolean = false
        var selectContents: Boolean =false
        var selectUsage : Boolean =false

        // 선택된 태그 값
        var fee :String ="" // 가격
        var contents: String="테스트 contemts" // 컨텐츠
        var usage: String="테스트 usage" // 지속 사용

        // TODO: 엉망코드~!! 수정해야함 !
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

        //수정하고 저장하기 버튼을 클릭한 경우(DB에 수정사항 저장)
        view.btn_save.setOnClickListener {
            //입력한 noteTitle, noteContent 가져와서 변수에 할당

            Log.d("입력한 값 : ", fee+" "+contents+" "+ usage)
            board.boardContent = view.txt_content.text.toString()
            board.subFee = fee
            board.subContents=contents
            board.usage=usage
            /* 제목과 내용이 다 있는지를 검증 */
            if (board.boardContent.isBlank() && board.boardContent.isBlank()) {
                Toast.makeText(requireContext(), "제목과 내용을 입력해주세요", Toast.LENGTH_LONG).show()
            } else {
                var userId: String? = ""
                FirebaseAuth.getInstance().currentUser?.let {
                    userId = it.email
                }

                /* 자동 스코프에 맞추어 코루틴을 실행*/
                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                    Log.d("subFee : ", board.subFee.toString())
                    val board = BoardEntity(
                        boardId = board.boardId,
                        boardContent = board.boardContent,
                        boardTitle = board.boardTitle,
                        boardImg = board.boardImg,
                        subFee = board.subFee,
                        usage = board.usage,
                        subContents = board.subContents,
                        userId = userId.toString()
                    )
                    if(updateYn.equals("Y")) {   // 신규글인지 수정인지 구분하여 저장
                        dao.updateBoard(board)
                    } else {
                        dao.insertBoard(board)//DB에 저장
                    }
                }
                findNavController().popBackStack()
            }
        }//end of view.btn_save.setOnClickListener
    }//end of onViewCreated


}