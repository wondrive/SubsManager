package com.example.subsmanager2.dao

import android.content.ContentValues.TAG
import android.util.Log
import com.example.subsmanager2.entity.ContentsBoardEntity
import com.google.android.material.tabs.TabLayout
import com.google.firebase.database.core.Tag
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

//게시글 DAO
class ContentsBoardDao {

    val Tag = "ContentsBoardDao"

    val db by lazy {Firebase.firestore}

    fun contensWriteBoard(data: ContentsBoardEntity) {

        var boardId: Int=0

        db.collection("count")
            .document("contents_board_count")
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("board id : ", document.get("count").toString())
                    boardId = Integer.parseInt(document.get("count").toString())
                    boardId +=1

                    //write
                    data.boardId = boardId.toLong()
                    Log.d("data,boardId:", data.boardId.toString())
                    db.collection("contents_board")
                        .document(data.boardId.toString())
                        .set(data)

                    //update count
                    db.collection("count")
                        .document("contents_board_count")
                        .update("count", FieldValue.increment(1))

                } else {
                    Log.d(Tag, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(Tag, "get failed with", exception)
            }
    }

}
