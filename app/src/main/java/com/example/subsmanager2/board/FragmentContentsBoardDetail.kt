package com.example.subsmanager2.board

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.subsmanager2.R
import com.example.subsmanager2.entity.ContentsBoardEntity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_contents_board_detail.view.*
import java.lang.Error

class FragmentContentsBoardDetail : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_contents_board_detail, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val boardId = arguments?.getString("boardId") ?: kotlin.run { throw Error("boardId가 없습니다") }
        val db by lazy { Firebase.firestore }
        Log.d("dao boardid:", boardId.toString())
        db.collection("contents_board")
            .document(boardId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null){
                val board = document.toObject(ContentsBoardEntity::class.java)
                if (board != null) {
                    view.content_txt_title.text=board.boardTitle
                    view.content_txt_content.text=board.boardContent
                    view.content_txt_story.text=board.contentsStory
                    view.content_txt_act.text=board.contentsAct
                    view.content_txt_restart.text=board.contentsRestart
                    view.content_txt_rating.text=board.ratingScore
                    view.item_txt_user_id.text=board.userId
                }
            }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with", exception)
            }
    }
}