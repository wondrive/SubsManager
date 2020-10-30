package com.example.subsmanager2.dao

import android.util.Log
import com.example.subsmanager2.entity.PlatformBoardEntity
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// 게시글 DAO (Data Access Object) : 데이터에 실제로 접근하는 명령 모음집
class PlatformBoardDao {

    val TAG = "PlatformBoardDao"

    val db by lazy {Firebase.firestore}

    // 게시글 작성 & platform_board_count 값을 증가해야함
    fun writeBoard( data: PlatformBoardEntity){
        var boardId : Int = 0

        db.collection("count")
            .document("platform_board_count")
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("board id : ", document.get("count").toString())
                    boardId = Integer.parseInt(document.get("count").toString())
                    boardId +=1

                    //wirte
                    data.boardId = boardId.toLong()
                    Log.d("data,boardId:",data.boardId.toString())
                    db.collection("platform_board")
                        .document(data.boardId.toString())
                        .set(data)

                    //update count num
                    db.collection("count")
                        .document("platform_board_count")
                        .update("count",FieldValue.increment(1))

                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }
    }


}



