package com.example.subsmanager2.board

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.subsmanager2.R
import com.example.subsmanager2.entity.BoardEntity
import kotlinx.android.synthetic.main.list_item_board.view.*


class BoardAdapter(var boardList: List<BoardEntity> = emptyList()) :
    RecyclerView.Adapter<BoardAdapter.ItemViewHolder>() {
    //note size 반환
    override fun getItemCount() = boardList.size

    /*뷰홀더 생성하여 반환*/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_board, parent, false)

        /* 뷰를 데이터와 맵핑하기위해 생성한 뷰홀더를 반환*/
        return ItemViewHolder(itemView)
    }

    //뷰홀더에 데이터를 바인딩
    override fun onBindViewHolder(holder: BoardAdapter.ItemViewHolder, position: Int) {
        //뷰홀더에 데이터를 바인딩하는 bindItems() 메서드 호출
        holder.bindItems(boardList[position])
    }

    //ItemViewHolder 클래스 선언
    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //아이템 뷰(list_item_note.xml)에 데이터 바인딩(noteTitle, noteImage)
        fun bindItems(board: BoardEntity) {
            /* Title 맵핑하기 */
            itemView.item_txt_title.text = board.boardTitle
            /* Note Image가 있다면?*/
            board.boardImg?.let {
                itemView.item_profile_image.visibility = View.VISIBLE
                //it: content://media/external/images/media/26 ==> 이미지 path
                Log.d("TAG", it)
                itemView.item_profile_image.setImageURI(Uri.parse(it))//이미지 설정
            } ?: kotlin.run {
                /* 없다면 지워주기*/
                itemView.item_profile_image.visibility = View.GONE
            }

            /* List 화면에서 아이템 뷰를 누르면 DetailFragment로 넘어감 */
            itemView.setOnClickListener {
                /* findNavController로 navigation을 찾아 설정된 Action 대로 화면을 전환
                   - List 화면에서 Detail 화면으로 전환
                   - Note의 id를 "NOTE_ID" 라는 이름으로 Bundel 객체 담아 DetailFragment에 전달
               */
                Navigation.findNavController(itemView).navigate(
                    R.id.action_boardListFragment_to_boardDetailFragment,
                    Bundle().apply {
                        /* //현재 선택한 Board의 id(primary key)
                          - noteIdx가 없는 리스트는 존재할 수 없으므로 강제 언래핑(Unwrapping)*/
                        putLong("BOARD_ID", board.boardId!!)
                        putString("USER_ID", board.userId!!)
                        putString("UPDATE_YN", "Y")
                    })
            }//end of setOnClickListener
        }//end of bindItems
    }//end of ItemViewHolder
}
