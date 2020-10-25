package com.example.subsmanager2.recommend

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.subsmanager2.R
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
        firestore?.collection("subs")
            ?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                // ArrayList 비워줌
                recommendList.clear()

                for (snapshot in querySnapshot!!.documents) {
                    var item = snapshot.toObject(RecommendEntity::class.java)
                    recommendList.add(item!!)
                }
                notifyDataSetChanged()
            }
    }

    /*뷰홀더 생성하여 반환*/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_subs, parent, false)
        Log.d("viewType value",viewType.toString())
        /* 뷰를 데이터와 맵핑하기위해 생성한 뷰홀더를 반환*/
        return ItemViewHolder(itemView)
    }

    //뷰홀더에 데이터를 바인딩
    override fun onBindViewHolder(holder: RecommendAdapter.ItemViewHolder, position: Int) {
        //뷰홀더에 데이터를 바인딩하는 bindItems() 메서드 호출
        Log.i(TAG, "getItemCount: "+getItemCount())
        holder.bindItems(recommendList[position])
    }

    //ItemViewHolder 클래스 선언
    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(recommend: RecommendEntity) {
            Log.i(TAG, "4. bindItems ::: " + recommend!!)
            /* 게시글 맵핑하기 */
            //if(recommend != null) {
            itemView.item_txt_fee.text = recommend.fee

            //itemView.item_txt_subs_name!!.text = recommend.subsName
        }//end of bindItems
    }//end of ItemViewHolder
}
