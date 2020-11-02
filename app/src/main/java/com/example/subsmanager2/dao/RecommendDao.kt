package com.example.subsmanager2.dao


import android.util.Log
import com.example.subsmanager2.entity.*
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

// 게시글 DAO (Data Access Object) : 데이터에 실제로 접근하는 명령 모음집
class RecommendDao {

    val TAG = "RecommendDao"

    private val resultList = ArrayList<RecommendEntity>()   // resultList
    fun resultList() = resultList   // getter

    //FirebaseAuth 인스턴스 추가 필요
    val firestore by lazy { FirebaseFirestore.getInstance() }

    fun countList(): Int {
        return resultList.size
    }

    // 맞춤형 서비스 출력           =====> 추후 userId로 roomDB에서 빈도높은 category 추출하여 검색하는 기능 추가
    fun selectRecommendList(selectCategory: String): ArrayList<RecommendEntity> {
        var recommend = RecommendEntity()

        var categoryQuery = firestore?.collection("category")
        var subsQuery = firestore?.collection("subs_official")
        var planQuery = firestore?.collection("plan_official")

        // 검색조건 = selectFee 보다 작은거 (추후 수정필요)
        // 1. category 테이블 : categoryId 추출
        var category = CategoryEntity()
        categoryQuery.document("1").get()
            .addOnSuccessListener {
                category = it.toObject(CategoryEntity::class.java)!!
                Log.i(
                    TAG,
                    "1. category ::: 카테고리 ID: " + category.id + ", 카테고리 이름 : " + category.name
                )

                // 2. subs_official 테이블 : subsId, subsName, imgUrl 추출
                subsQuery.whereEqualTo("categoryId", category.id)//category?.id.toLong())
                    ?.addSnapshotListener { subsSnapshot, firebaseFirestoreException ->
                        for (snapshot in subsSnapshot!!.documents) {
                            var subs = snapshot.toObject(SubsOfficialEntity::class.java)
                            //subsOfficialList.add(item!!)

                            Log.i(TAG, "2. subsOfficialList ::: " + subs!!.name)

                            // 3. plan_official 테이블 : 전체 추출
                            //if( ) { planQuery.whereLessThanOrEqualTo("fee", 0) }
                            planQuery.whereEqualTo("subsId", subs.id)//.orderBy("boardRating", Query.Direction.DESCENDING)//.orderBy("fee")
                                    ?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                                        for (snapshot in querySnapshot!!.documents) {
                                            var item = snapshot.toObject(PlanOfficialEntity::class.java)
                                            Log.i(TAG, "3. planOfficialList ::: " + item!!)

                                            // plan (요금제) 값 할당
                                            recommend.id = item.id
                                            recommend.planName = item.name
                                            recommend.fee = item.fee
                                            recommend.boardRating = item.boardRating
                                            // subs값 할당
                                            recommend.subsName = subs.name
                                            recommend.imgUrl = subs.imgUrl
                                            resultList.add(recommend!!)
                                            Log.i(TAG, "3-2. planOfficialList ::: " + recommend)
                                        }
                                    }
                        }
                    } // end of 2. subs_official 테이블
            }// end of 1. category 테이블*/

        return resultList();
    }

}



