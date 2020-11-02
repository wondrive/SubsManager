package com.example.subsmanager2.board

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.subsmanager2.R
import com.example.subsmanager2.entity.PlatformBoardEntity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_platform_board_detail.view.*


class FragmentPlatformBoardDetail : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView=inflater.inflate(R.layout.fragment_platform_board_detail, container, false)

        //adapter bundle로 넘긴 데이터 추출
        val boardId = arguments?.getString("boardId") ?: kotlin.run { throw Error("boardId이 없습니다.") }
        //firestore에서 게시판 정보 추출
        val db by lazy { Firebase.firestore}
        Log.d("dao boardid:",boardId.toString())
        db.collection("platform_board")
            .document(boardId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val board = document.toObject(PlatformBoardEntity::class.java)
                    if (board != null) {

                        rootView.item_txt_platform.text=board.boardTitle
                        rootView.item_txt_content.text=board.boardContent
                        rootView.item_txt_useage.text=board.usage
                        rootView.item_txt_rating.text=board.ratingScore
                        rootView.item_txt_fee.text=board.subFee
                        rootView.item_txt_user_id.text=board.userId
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }

        return rootView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

}
