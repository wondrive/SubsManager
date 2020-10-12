package com.example.subsmanager2.board

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Debug
import android.text.method.Touch.onTouchEvent
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
import com.kroegerama.imgpicker.BottomSheetImagePicker
import com.kroegerama.imgpicker.ButtonType
import kotlinx.android.synthetic.main.fragment_board_detail.view.*
import kotlinx.android.synthetic.main.fragment_board_write.*
import kotlinx.android.synthetic.main.fragment_board_write.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A simple [Fragment] subclass.
 */
class BoardWriteFragment : DialogFragment() {

    /* note 객체 생성 및 초기화. */
    private var board = BoardEntity(boardContent = "", boardTitle = "",userId = "",subFee = "",usage = "",boardImg = "")

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
        lateinit var testButton1: Button
        lateinit var testButton2: Button
        lateinit var testButton3: Button
        testButton1 = view.button18
        testButton2 = view.button19
        testButton3 = view.button20
        var changeColorRow1 : Boolean = false
        var changeColorRow2 : Boolean =false
        var changeColorRow3 : Boolean =false

        lateinit var testButton4: Button
        lateinit var testButton5: Button
        lateinit var testButton6: Button
        testButton4 = view.button21
        testButton5 = view.button22
        testButton6 = view.button23

        lateinit var testButton7: Button
        lateinit var testButton8: Button
        lateinit var testButton9: Button
        testButton7 = view.button7
        testButton8 = view.button16
        testButton9 = view.button20

        //
        var fee :String ="" // 가격
        var contents: String="" // 컨텐츠
        var usage: String="" // 지속 사용

        fun getTableRow() {
            testButton1.setOnTouchListener(object : View.OnTouchListener {
                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                    when (event?.action) {
                        MotionEvent.ACTION_DOWN -> {
                            Log.d("table row  ", "table row 1")
                        }
                    }
                    return true //or false
                }
            })
        }

        fun testClickBtn1(testButton1: Button, testButton2: Button,testButton3: Button){
            var changeColor:Boolean =false
            testButton1.setOnTouchListener(object : View.OnTouchListener {
                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                    when (event?.action) {
                        MotionEvent.ACTION_DOWN -> {
                            if(changeColor) {
                                    testButton2.setBackgroundResource(R.drawable.button_review)
                                    testButton3.setBackgroundResource(R.drawable.button_review)

                                    testButton1.setBackgroundResource(R.drawable.button_review_click)
                                    fee= testButton1.text as String
                                    changeColor = !changeColor
                                    Log.d("selectedItem ", fee.toString())
                            }
                            else{
                                changeColor = !changeColor
                                testButton1.setBackgroundResource(R.drawable.button_review)
                            }

                            Log.d("changecolor", changeColor.toString())
                        }
                    }
                    //리턴값이 false면 seekbar 동작 안됨
                    return true //or false
                }
            })

            testButton2.setOnTouchListener(object : View.OnTouchListener {
                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                    when (event?.action) {
                        MotionEvent.ACTION_DOWN -> {
                            if(changeColor) {
                                testButton1.setBackgroundResource(R.drawable.button_review)
                                testButton3.setBackgroundResource(R.drawable.button_review)
                                fee= testButton1.text as String
                                testButton2.setBackgroundResource(R.drawable.button_review_click)
                                changeColor = !changeColor
                            }
                            else{
                                changeColor = !changeColor
                                testButton2.setBackgroundResource(R.drawable.button_review)
                            }

                            Log.d("changecolor", changeColor.toString())
                        }
                    }
                    //리턴값이 false면 seekbar 동작 안됨
                    return true //or false
                }
            })

            testButton3.setOnTouchListener(object : View.OnTouchListener {
                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                    when (event?.action) {
                        MotionEvent.ACTION_DOWN -> {
                            if(changeColor) {
                                testButton1.setBackgroundResource(R.drawable.button_review)
                                testButton2.setBackgroundResource(R.drawable.button_review)

                                testButton3.setBackgroundResource(R.drawable.button_review_click)
                                fee= testButton1.text as String
                                changeColor = !changeColor
                            }
                            else{
                                changeColor = !changeColor
                                testButton3.setBackgroundResource(R.drawable.button_review)
                            }

                            Log.d("changecolor", changeColor.toString())
                        }
                    }
                    //리턴값이 false면 seekbar 동작 안됨
                    return true //or false
                }
            })
        }


        testClickBtn1(testButton1,testButton2,testButton3)
        testClickBtn1(testButton4,testButton5,testButton6)
        testClickBtn1(testButton7,testButton8,testButton9)


        //수정하고 저장하기 버튼을 클릭한 경우(DB에 수정사항 저장)
        view.btn_save.setOnClickListener {
            //입력한 noteTitle, noteContent 가져와서 변수에 할당
//            board.boardTitle = view.txt_title.text.toString()
            board.boardContent = view.txt_content.text.toString()
            board.subFee = fee
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
                        usage = "null",
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