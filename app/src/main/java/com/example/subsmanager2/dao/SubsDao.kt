package com.example.subsmanager2.dao

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.subsmanager2.entity.SubsEntity
import com.google.firebase.database.*
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.WriteBatch
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

// 게시글 DAO (Data Access Object) : 데이터에 실제로 접근하는 명령 모음집
class SubsDao {

    val TAG = "SubsDao"

    //db
    val db by lazy { Firebase.firestore}

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
    private val result: MutableLiveData<SubsEntity> = MutableLiveData()

    //resultList() getter 선언
    fun resultList(): LiveData<List<SubsEntity>> = resultList as MutableLiveData<List<SubsEntity>>
    fun result(): LiveData<SubsEntity> = result as MutableLiveData<SubsEntity>

    var subsList = ArrayList<SubsEntity>()            // subs 담아줄 리스트

    // 구독앱 갯수 (pk로 사용. 추가시 증가, 삭제시 줄어들지 않음)
    fun countSubs(): Int {  // subs/subsCount  값 받아오기
        return 0;
    }

    // 구독앱 목록
    fun selectSubsList(userId: String): LiveData<List<SubsEntity>> {
        subsList.clear()

        // 여러 문서 가져오기
        firestore.collection("subs").whereEqualTo("userId", userId).orderBy("subsId", Query.Direction.DESCENDING).get()
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

    fun selectLiveSubs(subsId: Long): LiveData<SubsEntity>? { //추후 not null 로 바꾸기
        Log.d("FireStore", "selectLiveSubs ::: "+subsId)
        firestore.collection("subs").whereEqualTo("subsId", subsId).get()
            .addOnSuccessListener { documentSnapshot ->
                for(document in documentSnapshot!!) {
                    Log.d(TAG, "Subs_Detail 성공 ::: " + document.data)
                    subs = document.toObject(SubsEntity::class.java)
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Subs 상세 읽기 실패: ", exception)
            }
        result.postValue(subs)
        return result;
    }

    // 구독앱 상세 ==> ㅋㅋ 안쓰는거..
    fun selectSubs(subsId: Long): SubsEntity? {
        // 하나만  가져오기
        firestore.collection("subs").whereEqualTo("subsId", subsId).get()
            .addOnSuccessListener { documentSnapshot ->
                for(document in documentSnapshot)
                    subs = document.toObject(SubsEntity::class.java)
                Log.d(TAG, "selectSubs 성공: "+subs.subsName)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "selectSubs 실패: ", exception)
            }

        return subs;
    }

    // 구독앱 등록
    fun insertSubs(subs: SubsEntity) {
        // pk로 지정할 subs_count 증분 작업
        firestore.collection("count").get().addOnCompleteListener {
                if(it.isSuccessful) { // 성공시
                    for (document in it.result!!) {
                        if(document.id == "subs_count") {
                            firestore.collection("count").document("subs_count")
                                .update("count", FieldValue.increment(1))
                            // 형식: document.id == "subs_count", document.data == {count: 숫자}
                            subs.subsId = document.data["count"].toString().toLong()
                            Log.d("FireStore", "subs_count 증분 성공 ::: " + subs.subsId)

                            // 최종 저장
                            firestore.collection("subs").document(subs.subsId.toString()).set(subs)
                                .addOnSuccessListener { Log.d("FireStore", "Subs Insert 성공") }
                                .addOnFailureListener { Log.w("Firestore", "Subs Insert 실패", it) }
                        }
                    }
                }
                else {
                    Log.w("Firestore", "subs_count :: 실패", it.exception)
                }
            }
    }

    fun insertSubsList(subsList: ArrayList<SubsEntity>) {
        val countRef = firestore.collection("count") //.document("subs_count")
        val subsRef = firestore.collection("subs")
        val batch = firestore.batch()
        var subs_count = 0L

        firestore.runTransaction { transaction ->
            // count 값 가져오기 1
            val snapshot = transaction.get(countRef.document("subs_count"))
            subs_count = snapshot.getLong("count")!!
            Log.d("FireStore", "subs_count 추출 ::: " + subs_count)

            // count 값 가져오기2
            /*countRef.get().addOnCompleteListener {
                if (it.isSuccessful) { // 성공시
                    for (document in it.result!!) {
                        if (document.id == "subs_count") {
                            subs_count = document.data["count"].toString().toLong()
                            Log.d("FireStore", "subs_count 증분 성공 ::: " + subs_count)
                        }
                    }
                }
            }*/


            for(item in subsList) {
                item.subsId = ++subs_count
                transaction.set(subsRef.document(item.subsId.toString()), item)

                Log.d(TAG, "subsList 추가 후 subs_count: "+subs_count)
            }

            // subs_count를 update
            //countRef.document("subs_count").update("count", subs_count)
            transaction.update(countRef.document("subs_count"), "count", subs_count)

            //batch.commit()

            Log.d(TAG, "최종: "+subs_count)
        }

    }

    // 업데이트
    fun updateSubs(subs: SubsEntity) {
        db.collection("subs")
            .document(subs.subsId.toString())
            .set(subs)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
    }

    // 삭제
    fun deleteSubs(subsId: Long) {
        db.collection("subs")
            .document(subsId.toString())
            .delete()
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
    }

}



