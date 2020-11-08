package com.example.subsmanager2.subs

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.appcompat.widget.SwitchCompat
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import com.example.subsmanager2.R
import com.example.subsmanager2.dao.SubsDao
import com.example.subsmanager2.entity.SubsEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_mypage.view.*
import kotlinx.android.synthetic.main.list_item_subs.view.*


//class SubsAdapter(var subsList: List<SubsEntity> = emptyList()) :
class SubsAdapter : RecyclerView.Adapter<SubsAdapter.ItemViewHolder>() {

    //현재 로그인한 유저정보를 가져옴
    var user =""
    // DAO
    val subsDao = SubsDao()
    var subsList = ArrayList<SubsEntity>()

    override fun getItemCount() = subsList.size

    // 새로 추가한 부분 2020-10-19 by 정승원
    // 현재 사용자가 등록한 구독앱 목록을 가져옴 2020-11-01 by 박진아
    init {  // firebase에서 subs 불러온 뒤 Entity로 변환해 ArrayList에 담음

        //현재 사용자 정보
        FirebaseAuth.getInstance().currentUser?.let { it ->
            user=it.email.toString()
            Log.d("user adapter:",user)
        }

        //이용자가 등록한 서비스 목록
        subsDao.firestore?.collection("subs")
            .orderBy("subsId", Query.Direction.DESCENDING)
            .whereEqualTo("userId",user.toString())
            ?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                // ArrayList 비워줌
                subsList.clear()
                for (snapshot in querySnapshot!!.documents) {
                    var item = snapshot.toObject(SubsEntity::class.java)
                    if (item != null) {
                        subsList.add(item!!)
                    }
                }
                notifyDataSetChanged()
            }
    }

    /*뷰홀더 생성하여 반환*/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_subs, parent, false)

        /* 뷰를 데이터와 맵핑하기위해 생성한 뷰홀더를 반환*/
        return ItemViewHolder(itemView)
    }

    //뷰홀더에 데이터를 바인딩
    override fun onBindViewHolder(holder: SubsAdapter.ItemViewHolder, position: Int) {
        //뷰홀더에 데이터를 바인딩하는 bindItems() 메서드 호출
        holder.bindItems(subsList[position])
    }

    //ItemViewHolder 클래스 선언
    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //아이템 뷰(list_item_note.xml)에 데이터 바인딩(noteTitle, noteImage)
        fun bindItems(subs: SubsEntity) {
            /* 게시글 맵핑하기 */
            itemView.item_txt_nickname.text = subs.subsCustomName
            itemView.item_txt_name.text = subs.subsName
            /*itemView.item_txt_fee.text = subs.fee
            itemView.item_txt_feedate.text = subs.feeDate*/
            itemView.item_switch_alarm.isChecked = subs.alarmYN

            /* List 화면에서 아이템 뷰를 누르면 DetailFragment로 넘어감 */
            itemView.setOnClickListener {
                var index = adapterPosition
                if (index != NO_POSITION) {
                    Log.d("index:",index.toString())
                    Navigation.findNavController(itemView).navigate(
                        R.id.action_subsListFragment_to_subsDetailFragment,
                        Bundle().apply {
                            putLong("SUBS_ID", subs.subsId!!)
                            putString("USER_ID", subs.userId!!)
                            putString("UPDATE_YN", "Y")
                        })
                }
            }//end of setOnClickListener

            itemView.item_switch_alarm.setOnCheckedChangeListener{ buttonView, isChecked ->
                subs.alarmYN = itemView.item_switch_alarm.isChecked
                subsDao.updateSubs(subs)
            }
        }//end of bindItems
    }//end of ItemViewHolder
}

