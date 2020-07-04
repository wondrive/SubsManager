package com.example.homecookhelper.agric

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.homecookhelper.R
import com.example.homecookhelper.dao.AgricDao
import com.example.homecookhelper.entity.AgricEntity
import kotlinx.android.synthetic.main.list_item_agric.view.*
import java.io.BufferedInputStream
import java.io.IOException
import java.net.URL
import java.net.URLConnection

// 저장된 제철 농산물을 리사이클러뷰에 보여주는 어댑터
class AgricAdapter() : RecyclerView.Adapter<AgricAdapter.ItemViewHolder>(){
    
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
        //holder.bindItems(agricList[position])
    }

    //ItemViewHolder 클래스 선언
    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //데이터 바인딩(agricName, agricImage)
        
        fun bindItems(agric: AgricEntity) {
            agric?.let {
                itemView.txt_agric_name.text = agric.agricName

                // 이미지 있으면 보여주기
                agric.imgUrl?.let {
                    itemView.img_agric.visibility = View.VISIBLE
                    //it: content://media/external/images/media/26 ==> 이미지 path
                    Log.d("TAG", it)
                    //itemView.img_agric.setImageURI(Uri.parse(it))//이미지 설정

                    // 이미지 인터넷에서 가져오기
                    try {
                        var conn: URLConnection = URL(agric.imgUrl).openConnection()
                        conn.connect()

                        var bs: BufferedInputStream = BufferedInputStream(conn.getInputStream())
                        var bm: Bitmap = BitmapFactory.decodeStream(bs)
                        itemView.img_agric.setImageBitmap(bm)
                        bs.close()
                    } catch (e: IOException) {
                        Log.d("ERROR", it)
                    }
                } ?: kotlin.run {
                    /* 없다면 지워주기*/
                    itemView.img_agric.visibility = View.GONE
                }

            }

            /* List 화면에서 아이템 뷰를 누르면 DetailFragment로 넘어감 */
            itemView.setOnClickListener {
                /* findNavController로 navigation을 찾아 설정된 Action 대로 화면을 전환
                   - List 화면에서 Detail 화면으로 전환
                   - Note의 id를 "NOTE_ID" 라는 이름으로 Bundel 객체 담아 DetailFragment에 전달
               */
                Navigation.findNavController(itemView).navigate(
                    R.id.action_agricListFragment_to_agricDetailFragment,
                    Bundle().apply {
                        //현재 선택한 Agric의 id(primary key), 강제 언래핑(Unwrapping)
                        putString("AGRIC_ID", agric.agricId!!)
                    })
            }//end of setOnClickListener
        }//end of bindItems
    }//end of ItemViewHolder

}

