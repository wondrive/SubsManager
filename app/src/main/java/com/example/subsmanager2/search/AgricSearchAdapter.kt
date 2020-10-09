package com.example.subsmanager2.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.subsmanager2.R
import com.example.subsmanager2.entity.AgricEntity
import kotlinx.android.synthetic.main.list_item_agric.view.*

class AgricSearchAdapter() : RecyclerView.Adapter<AgricSearchAdapter.ItemViewHolder>() {

    // 받아온 데이터를 저장할 list
    var agricList: List<AgricEntity> = ArrayList()

    // 어뎁터에서 관리할 아이템 갯수 반환
    override fun getItemCount() = agricList.size

    // 뷰홀더 생성해서 반환
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_agric, parent, false)
        // 뷰를 데이터와 맵핑하기 위해 생성한 뷰홀더 반환
        return ItemViewHolder(itemView)
    }

    //뷰홀더에 데이터를 바인딩
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        //뷰홀더에 데이터를 바인딩하는 bindItems() 메서드 호출
        holder.bindItems(agricList[position])
    }

    //ItemViewHolder 클래스 선언
    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //데이터 바인딩(agricName, agricImage)

        fun bindItems(agric: AgricEntity) {
            agric?.let {
                itemView.txt_agric_name.text = agric.agricName
            }
            // Search 화면에서 아이템 뷰를 누르면 DetailFragment로 넘어감
            itemView.setOnClickListener {
                Navigation.findNavController(itemView).navigate(
                    R.id.action_agricSearchFragment_to_agricDetailFragment,
                    Bundle().apply {
                        putString("AGRIC_NAME", agric.agricName!!)
                    })
            }//end of setOnClickListener
        }//end of bindItems
    }//end of ItemViewHolder*/
}
