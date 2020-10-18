package com.example.subsmanager2.dao

import android.util.Log
import com.example.subsmanager2.entity.SubsEntity
import com.example.subsmanager2.entity.UserEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.android.synthetic.main.fragment_mypage.view.*

// 게시글 DAO (Data Access Object) : 데이터에 실제로 접근하는 명령 모음집
class UserDao {

    val TAG = "UserDao"

    // JSON 라이브러리 모시(moshi) 플러그인
    val moshi by lazy {
        //data class를 JSON처럼 다룰 수 있도록 해주는 Moshi 플러그인
        Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    }
    private var user = UserEntity()

    //Firebase
    val firestore by lazy { FirebaseFirestore.getInstance() }


    // 회원 상세
    fun selectUser(userId: Long): UserEntity? {
        // 하나만  가져오기
        firestore.collection("user").whereEqualTo("userId", userId).get()
            .addOnSuccessListener { documentSnapshot ->
                for(document in documentSnapshot)
                    user = document.toObject(UserEntity::class.java)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "상세 읽기 실패: ", exception)
            }

        return user;
    }

    // 회원 등록
    fun insertUser(user: UserEntity) {
        // pk로 지정할 subs_count 증분 작업
        firestore.collection("count").get().addOnCompleteListener {
                if(it.isSuccessful) { // 성공시
                    for (document in it.result!!) {
                        if(document.id == "user_count") {
                            firestore.collection("count").document("user_count").update("user_count", FieldValue.increment(1))
                            // 형식: document.id == "subs_count", document.data == {count: 숫자}
                            Log.d(TAG, "user_count 증분 성공 ::: " + document.data)
                            user.userNo = document.data["user_count"].toString().toLong()
                            Log.d(TAG, "user_count 증분 성공 ::: " + user.userNo)

                            // 최종 회원 정보 입력
                            firestore.collection("user").document(user.userNo.toString()).set(user)
                                .addOnSuccessListener { Log.d(TAG, "User Insert 성공") }
                                .addOnFailureListener { Log.w(TAG, "User Insert 실패", it) }
                        }
                    }
                }
                else {
                    Log.w(TAG, "user_count 증분 실패", it.exception)
                }
            }

        // Firebase 실시간DB : 데이터 입력
        //var userId: String  = edit_id.text.toString().replace("@", "_").replace(".", "_") // 사용자 이메일에서 특수문자(@, .) 제거

        //firebaseRef.child("user").push().setValue(dataInput)
    }

    // 업데이트
    fun updateSubs(subs: SubsEntity) {
        //firebaseRef.child("subs").child(subs.subsId.toString()).setValue(subs)

    }

    // 삭제
    fun deleteUser(userNo: Long) {}
}



