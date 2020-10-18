package com.example.subsmanager2.dao

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.subsmanager2.entity.AgricEntity
import com.example.subsmanager2.entity.AgricWrapper
import com.example.subsmanager2.entity.SubsEntity
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.*
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.auth.User
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.json.JSONObject
import org.w3c.dom.Comment

// 게시글 DAO (Data Access Object) : 데이터에 실제로 접근하는 명령 모음집
class SubsDao {

    val TAG = "FireStore"

    // JSON 라이브러리 모시(moshi) 플러그인
    val moshi by lazy {
        //data class를 JSON처럼 다룰 수 있도록 해주는 Moshi 플러그인
        Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    }
    private var subs = SubsEntity()

    //FirebaseAuth 객체의 공유 인스턴스를 가져오기
    val firebaseRef by lazy { FirebaseDatabase.getInstance().getReference() }
    val firestore by lazy { FirebaseFirestore.getInstance() }

    //resultList LiveData 선언
    private val resultList: MutableLiveData<List<SubsEntity>> = MutableLiveData()
    //resultList() getter 선언
    fun resultList(): LiveData<List<SubsEntity>> = resultList as MutableLiveData<List<SubsEntity>>

    // 구독앱 갯수 (pk로 사용. 추가시 증가, 삭제시 줄어들지 않음)
    fun countSubs(): Int {  // subs/subsCount  값 받아오기
        return 0;
    }

    // 구독앱 목록
    fun selectSubsList(): LiveData<List<SubsEntity>> {    // not null로 추후 수정
        var subsList = ArrayList<SubsEntity>()            // subs 담아줄 리스트
        /*val listener = object : ValueEventListener{
            // 데이터 읽기 성공
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for(subsSnapshot in dataSnapshot.children) {
                    val data: SubsEntity? = subsSnapshot.getValue(SubsEntity::class.java)

                    if(data != null) {
                        subs.subsName = data.subsName
                        subs.subsCustomName = data.subsCustomName
                        subs.subsId = data.subsId
                        subs.subsName = data.subsName

                        subsList.add(subs)
                    }
                }
                //resultList(LiveData) 저장
                resultList.postValue(subsList)
            }
            // 데이터 읽기 실패
            override fun onCancelled(error: DatabaseError) {
                Log.w("FirebaseDB","loadSubsList:onCancelled", error.toException());
            }
        }
        // Firebase 실시간 DB : 데이터 가져오기      // orderBy(): subs_id순으로 정렬. listener: DB수정할때마다 목록 안바뀌게 함
        var test = firebaseRef.child("subs").orderByChild("subs_id").limitToLast(2).addListenerForSingleValueEvent(listener)
        Log.i("FirebaseDB","가져온 값..? :: "+test.toString());*/


        /*val query = firestore.collection("subs").orderBy("subsId", Query.Direction.DESCENDING)
        val listener = OnSuccessListener<DocumentSnapshot>() {
            @Override
            fun onSuccess(documentSnapshot: DocumentSnapshot) {
                var result = documentSnapshot.toObject(SubsEntity::class.java);
                if (result == null) {

                } else {
                    //writeNewPost(userId, user.username, title, body);

                }

            }
        }
        docRef.get().addOnSuccessListener(listener)*/


        // 여러 문서 가져오기
        firestore.collection("subs").orderBy("subsId", Query.Direction.DESCENDING).get()
            .addOnSuccessListener { documents ->
                for (document in documents!!) {
                    Log.d(TAG, "Subs_List 출력 ::: " + document.data)
                    subs = document.toObject(SubsEntity::class.java)
                    subsList.add(subs)
                }
                resultList.postValue(subsList)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }

        return resultList;
    }

    fun selectLiveBoard(boardId: Long): LiveData<SubsEntity>? { //추후 not null 로 바꾸기
        return null;
    }

    // 구독앱 상세
    fun selectSubs(subsId: Long): SubsEntity? {      //아직 미완성 ㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠㅠ
        //var subs = firebaseRef.child("subs").child(subsId.toString())

        // 하나만  가져오기
        /*val docRef = firestore.collection("subs").whereEqualTo("subsId", subsId)
        docRef.get().addOnSuccessListener { documentSnapshot ->
            val subs = documentSnapshot.toObject(SubsEntity::class.java)
        }*/


        return null;
    }

    // 구독앱 등록
    fun insertSubs(subs: SubsEntity) {
        // pk로 지정할 subs_count 증분 작업
        firestore.collection("count").get().addOnCompleteListener {
                if(it.isSuccessful) { // 성공시
                    for (document in it.result!!) {
                        firestore.collection("count").document("subs_count").update("count", FieldValue.increment(1))
                        // 형식: document.id == "subs_count", document.data == {count: 숫자}
                        subs.subsId = document.data["count"].toString().toLong()
                        Log.d("FireStore", "subs_count 증분 성공 ::: "+subs.subsId)

                        // 최종 저장
                        firestore.collection("subs").document(subs.subsId.toString()).set(subs)
                            .addOnSuccessListener { Log.d("FireStore", "Subs Insert 성공") }
                            .addOnFailureListener { Log.w("Firestore", "Subs Insert 실패", it) }
                    }
                }
                else {
                    Log.w("Firestore", "subs_count :: 실패", it.exception)
                }
            }
    }

    // 업데이트
    fun updateSubs(subs: SubsEntity) {
        firebaseRef.child("subs").child(subs.subsId.toString()).setValue(subs)
    }

    // 삭제
    fun deleteSubs(subsId: Long) {
        /*firebaseRef.child("subs").child(subsId.toString()).setValue(null)*/

        /*db.collection("cities").document("DC")
            .delete()
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }*/
    }

}



