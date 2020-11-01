package com.example.subsmanager2.board

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.subsmanager2.R
import com.example.subsmanager2.dao.ContentsBoardDao
import com.example.subsmanager2.entity.ContentsBoardEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_contents_board_write.*
import kotlinx.android.synthetic.main.fragment_contents_board_write.view.*
import kotlinx.android.synthetic.main.fragment_contents_board_write.view.btn_save
import kotlinx.android.synthetic.main.fragment_contents_board_write.view.rating_bar
import kotlinx.android.synthetic.main.fragment_contents_board_write.view.txt_content
import kotlinx.android.synthetic.main.fragment_join.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class contentsBoardWriteFragment : DialogFragment() {

    private var board = ContentsBoardEntity(boardContent = "", userId = "", boardTitle = "", contentsStory = "", contentsAct = "", contentsRestart = "", boardCreateDt = "", ratingScore = "")
    val boardDao = ContentsBoardDao()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_contents_board_write, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //별점
        lateinit var rating_bar: RatingBar
        lateinit var txt_rating_bar: TextView
        var ratingScore = ""
        rating_bar = view.rating_bar
        txt_rating_bar = view.txt_rating_bar

        rating_bar.setOnRatingBarChangeListener {ratingBar, rating, fromUser ->
            txt_rating_bar.text = rating.toString()
            ratingScore = rating.toString()
            Log.d("ratingscore", ratingScore)

        }

        var selectStory : Boolean = false
        var selectAct : Boolean = false
        var selectRestart : Boolean = false

        var contentsName : String=""
        var story: String=""
        var acting: String=""
        var restart: String=""

        lateinit var btn_story1: Button
        lateinit var btn_story2: Button
        lateinit var btn_story3: Button
        btn_story1 = view.btn_story1
        btn_story2 = view.btn_story2
        btn_story3 = view.btn_story3

        lateinit var btn_acting1: Button
        lateinit var btn_acting2: Button
        lateinit var btn_acting3: Button
        btn_acting1 = view.btn_acting1
        btn_acting2 = view.btn_acting2
        btn_acting3 = view.btn_acting3

        lateinit var btn_restart1: Button
        lateinit var btn_restart2: Button
        btn_restart1 = view.btn_restart1
        btn_restart2 = view.btn_restart2

        fun selectStoryBtn(btn_story1 : Button, btn_story2 : Button, btn_story3: Button){
            btn_story1.setOnTouchListener(object : View.OnTouchListener {
                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                    when (event?.action) {
                        MotionEvent.ACTION_DOWN -> {
                            if (selectStory) {
                                btn_story2.setBackgroundResource(R.drawable.button_review)
                                btn_story3.setBackgroundResource(R.drawable.button_review)

                                btn_story1.setBackgroundResource(R.drawable.button_review_click)
                                story = btn_story1.text as String
                                selectStory = !selectStory
                            } else {
                                selectStory = !selectStory
                                btn_story1.setBackgroundResource(R.drawable.button_review)
                            }
                        }

                    }
                    return true
                }
            })

            btn_story2.setOnTouchListener(object : View.OnTouchListener {
                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                    when (event?.action) {
                        MotionEvent.ACTION_DOWN -> {
                            if (selectStory) {
                                btn_story1.setBackgroundResource(R.drawable.button_review)
                                btn_story3.setBackgroundResource(R.drawable.button_review)

                                btn_story2.setBackgroundResource(R.drawable.button_review_click)
                                story = btn_story2.text as String
                                selectStory = !selectStory
                            } else {
                                selectStory = !selectStory
                                btn_story2.setBackgroundResource(R.drawable.button_review)
                            }
                        }

                    }
                    return true
                }
            })

            btn_story3.setOnTouchListener(object : View.OnTouchListener {
                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                    when (event?.action) {
                        MotionEvent.ACTION_DOWN -> {
                            if (selectStory) {
                                btn_story1.setBackgroundResource(R.drawable.button_review)
                                btn_story2.setBackgroundResource(R.drawable.button_review)

                                btn_story3.setBackgroundResource(R.drawable.button_review_click)
                                story = btn_story3.text as String
                                selectStory = !selectStory
                            } else {
                                selectStory = !selectStory
                                btn_story3.setBackgroundResource(R.drawable.button_review)
                            }
                        }

                    }
                    return true
                }
            })

        }

        fun selectActBtn(btn_acting1 : Button, btn_acting2 : Button, btn_acting3: Button){
            btn_acting1.setOnTouchListener(object : View.OnTouchListener {
                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                    when (event?.action) {
                        MotionEvent.ACTION_DOWN -> {
                            if (selectAct) {
                                btn_acting2.setBackgroundResource(R.drawable.button_review)
                                btn_acting3.setBackgroundResource(R.drawable.button_review)

                                btn_acting1.setBackgroundResource(R.drawable.button_review_click)
                                acting = btn_acting1.text as String
                                selectAct = !selectAct
                            } else {
                                selectAct = !selectAct
                                btn_acting1.setBackgroundResource(R.drawable.button_review)
                            }
                        }

                    }
                    return true
                }
            })

            btn_acting2.setOnTouchListener(object : View.OnTouchListener {
                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                    when (event?.action) {
                        MotionEvent.ACTION_DOWN -> {
                            if (selectAct) {
                                btn_acting1.setBackgroundResource(R.drawable.button_review)
                                btn_acting3.setBackgroundResource(R.drawable.button_review)

                                btn_acting2.setBackgroundResource(R.drawable.button_review_click)
                                acting = btn_acting2.text as String
                                selectAct = !selectAct
                            } else {
                                selectAct = !selectAct
                                btn_acting2.setBackgroundResource(R.drawable.button_review)
                            }
                        }

                    }
                    return true
                }
            })

            btn_acting3.setOnTouchListener(object : View.OnTouchListener {
                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                    when (event?.action) {
                        MotionEvent.ACTION_DOWN -> {
                            if (selectAct) {
                                btn_acting1.setBackgroundResource(R.drawable.button_review)
                                btn_acting2.setBackgroundResource(R.drawable.button_review)

                                btn_acting3.setBackgroundResource(R.drawable.button_review_click)
                                acting = btn_acting3.text as String
                                selectAct = !selectAct
                            } else {
                                selectAct = !selectAct
                                btn_acting3.setBackgroundResource(R.drawable.button_review)
                            }
                        }

                    }
                    return true
                }
            })

        }

        fun selectRestartBtn(btn_restart1: Button, btn_restart2: Button){
            btn_restart1.setOnTouchListener(object : View.OnTouchListener{
                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                    when (event?.action) {
                        MotionEvent.ACTION_DOWN -> {
                            if (selectRestart) {
                                btn_restart2.setBackgroundResource(R.drawable.button_review)
                                btn_restart1.setBackgroundResource(R.drawable.button_review_click)
                                restart = btn_restart1.text as String
                                selectRestart = !selectRestart
                            } else {
                                selectRestart = !selectRestart
                                btn_restart1.setBackgroundResource(R.drawable.button_review)
                            }
                        }

                    }
                    return true

                }
            })

            btn_restart2.setOnTouchListener(object : View.OnTouchListener{
                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                    when (event?.action) {
                        MotionEvent.ACTION_DOWN -> {
                            if (selectRestart) {
                                btn_restart1.setBackgroundResource(R.drawable.button_review)
                                btn_restart2.setBackgroundResource(R.drawable.button_review_click)
                                restart = btn_restart2.text as String
                                selectRestart = !selectRestart
                            } else {
                                selectRestart = !selectRestart
                                btn_restart2.setBackgroundResource(R.drawable.button_review)
                            }
                        }

                    }
                    return true

                }
            })
        }

        selectStoryBtn(btn_story1,btn_story2,btn_story3)
        selectActBtn(btn_acting1,btn_acting2,btn_acting3)
        selectRestartBtn(btn_restart1,btn_restart2)

        if (selectStory && selectAct && selectRestart){
            btn_save.setBackgroundColor(R.drawable.button_review_click)
        }

        fun setCreateDt(): String{
            val cal = Calendar.getInstance()
            cal.time=Date()
            val df: DateFormat = SimpleDateFormat("yyyy-mm-dd")
            return df.format(cal.time)
        }

        view.btn_save.setOnClickListener{
            val spinner_contents_list : Spinner = view.spinner_contents_list
            contentsName = spinner_contents_list.selectedItem.toString()

            board.boardTitle = contentsName
            board.boardContent = view.txt_content.text.toString()
            board.contentsStory = story
            board.contentsAct = acting
            board.contentsRestart = restart

            if (selectStory || selectAct || selectRestart) {
                Toast.makeText(requireContext(), "필수항목을 작성해주세요!", Toast.LENGTH_LONG).show()
            } else {
                var userId: String? = ""
                FirebaseAuth.getInstance().currentUser?.let {
                    userId = it.email
                }

                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO){
                    val board = ContentsBoardEntity(
                        boardContent = board.boardContent,
                        boardTitle = board.boardTitle,
                        contentsStory = board.contentsStory,
                        contentsAct = board.contentsAct,
                        contentsRestart = board.contentsRestart,
                        userId = userId.toString(),
                        boardCreateDt = setCreateDt(),
                        ratingScore = ratingScore
                    )
                    boardDao.contensWriteBoard(data = board)
                }
            }
            findNavController().popBackStack()
        }
    }
}
