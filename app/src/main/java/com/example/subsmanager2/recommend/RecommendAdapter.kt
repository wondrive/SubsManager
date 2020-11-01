package com.example.subsmanager2.recommend

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.subsmanager2.R
import com.example.subsmanager2.dao.RecommendDao
import com.example.subsmanager2.entity.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.list_item_recommend.view.*


//class SubsAdapter(var subsList: List<SubsEntity> = emptyList()) :
class RecommendAdapter : RecyclerView.Adapter<RecommendAdapter.ItemViewHolder>() {

    var TAG = "RecommendAdapter"
    // DAO
    var recommendList = ArrayList<RecommendEntity>()

    val firestore by lazy { FirebaseFirestore.getInstance() }

    override fun getItemCount() = recommendList.size

    // 추가된 부분
    init {  // firebase에서 subs 불러온 뒤 Entity로 변환해 ArrayList에 담음
        recommendList.clear()

        Log.i(TAG, "  init")
        //var subsOfficialList = ArrayList<SubsOfficialEntity>()
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
                Log.i(TAG, "1. category ::: 카테고리 ID: " + category.id + ", 카테고리 이름 : " + category.name)

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
                                        recommendList.add(recommend!!)
                                        Log.i(TAG, "3-2. recommend ::: " + recommend)
                                        Log.i(TAG, "3-3. recommendList ::: " + recommendList)
                                    }
                                    notifyDataSetChanged()  // 위치 꼭 여기여야 만함
                                    Log.i(TAG, "4. recommendList ::: " + recommendList)
                                }
                        }
                    } // end of 2. subs_official 테이블
            }// end of 1. category 테이블*/

    }

    /*뷰홀더 생성하여 반환*/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_recommend, parent, false)
        /* 뷰를 데이터와 맵핑하기위해 생성한 뷰홀더를 반환*/
        return ItemViewHolder(itemView)
    }

    //뷰홀더에 데이터를 바인딩
    override fun onBindViewHolder(holder: RecommendAdapter.ItemViewHolder, position: Int) {
        //뷰홀더에 데이터를 바인딩하는 bindItems() 메서드 호출
        //Log.i(TAG, "getItemCount: "+getItemCount())
        holder.bindItems(recommendList[position])
    }

    //ItemViewHolder 클래스 선언
    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(recommend: RecommendEntity) {
            recommend?.let {
                Log.i(TAG, "bindItems ::: " + recommend)
                /* 게시글 맵핑하기 */
                itemView.item_txt_plan_name.text = recommend.planName
                itemView.item_txt_subs_name.text = recommend.subsName
                itemView.item_rating.rating = recommend.boardRating.toFloat()
                itemView.item_txt_fee.text = recommend.fee+"￦"

                // 이미지 추후 처리
                //itemView.img_sub_app.setImageURI(recommend.imgUrl.toUri())
            }
        }//end of bindItems
    }//end of ItemViewHolder
}
