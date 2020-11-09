package com.example.subsmanager2.recommend

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.subsmanager2.R
import com.example.subsmanager2.entity.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.list_item_platform_board.view.*
import kotlinx.android.synthetic.main.list_item_recommend.view.*
import kotlinx.android.synthetic.main.list_item_recommend.view.img_sub_app
import kotlinx.android.synthetic.main.list_item_recommend.view.item_txt_fee


//class SubsAdapter(var subsList: List<SubsEntity> = emptyList()) :
class RecommendAdapter : RecyclerView.Adapter<RecommendAdapter.ItemViewHolder>() {

    var TAG = "RecommendAdapter"
    // DAO
    var recommendList = ArrayList<PlanOfficialEntity>()
    val firestore by lazy { FirebaseFirestore.getInstance() }

    override fun getItemCount() = recommendList.size

    // 추가된 부분
    init {  // firebase에서 subs 불러온 뒤 Entity로 변환해 ArrayList에 담음
        recommendList.clear()

        Log.i(TAG, "  init")
        //var subsOfficialList = ArrayList<SubsOfficialEntity>()
        var recommend = PlanOfficialEntity()
        var planQuery = firestore?.collection("plan_official")
        //PLANQUERY 만 사용해서 돌려보기

        // plan_offical 테이블
        var plan = PlanOfficialEntity()
        planQuery.whereEqualTo("categoryName", searchword)
            //.orderBy("boardRating", Query.Direction.DESCENDING)       // 색인추가로 해결
            //.orderBy("fee")
            ?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                for (snapshot in querySnapshot!!.documents) {
                    var item = snapshot.toObject(PlanOfficialEntity::class.java)
                    Log.i(TAG, "3. planOfficialList ::: " + item!!)
                    // plan (요금제) 값 할당
                    recommendList.add(item!!)
                }
                notifyDataSetChanged()  // 위치 꼭 여기여야 만함
                Log.i(TAG, "4. recommendList ::: " + recommendList)
            }

    }// end of init {}




    fun setSubAppIcon(appName: String?, img_sub_app: ImageView) {
        when (appName) {
            "넷플릭스" -> img_sub_app.setImageResource(R.drawable.netflix)
            "왓챠" -> img_sub_app.setImageResource(R.drawable.watcha)
            "유튜브 프리미엄" -> img_sub_app.setImageResource(R.drawable.youtube)
            "밀리의 서재" -> img_sub_app.setImageResource(R.drawable.millie)
            "지니뮤직" -> img_sub_app.setImageResource(R.drawable.genie)
            "멜론뮤직" -> img_sub_app.setImageResource(R.drawable.melon)
            "리디북스" -> img_sub_app.setImageResource(R.drawable.ridi)
        }
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
        fun bindItems(recommend: PlanOfficialEntity) {


                Log.i(TAG, "bindItems ::: " + recommend)
                /* 게시글 맵핑하기 */
                itemView.item_txt_plan_name.text = recommend.name
                itemView.item_txt_subs_name.text = recommend.subsName
                itemView.item_rating.rating = recommend.boardRating.toFloat()
                setSubAppIcon(recommend.subsName, itemView.img_sub_app)
                itemView.item_txt_fee.text = recommend.fee.toString()+"￦"


                // 이미지 추후 처리
                //itemView.img_sub_app.setImageURI(recommend.imgUrl.toUri())

        }//end of bindItems
    }//end of ItemViewHolder
    companion object {var searchword = "영화/드라마"
    }fun setSearchWord(a: String) {searchword = a};
}
