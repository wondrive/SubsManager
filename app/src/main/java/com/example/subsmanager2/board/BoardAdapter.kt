package com.example.subsmanager2.board

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
    //ToDO 반환 아이템은 최대 3개
    override fun getItemCount() = boardList.size
    var debug = printDebug()
    fun printDebug(){
        Log.d("debug board adapter",boardList.size.toString())
    }


    /*뷰홀더 생성하여 반환*/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_board, parent, false)
        Log.d("viewType value",viewType.toString())
        /* 뷰를 데이터와 맵핑하기위해 생성한 뷰홀더를 반환*/
        return ItemViewHolder(itemView)
    }

    //뷰홀더에 데이터를 바인딩
    override fun onBindViewHolder(holder: BoardAdapter.ItemViewHolder, position: Int) {
        Log.d("get count","1");
        Log.d("position",position.toString());
        //뷰홀더에 데이터를 바인딩하는 bindItems() 메서드 호출
        if(position<=2){
            holder.bindItems(boardList[position])
        }
    }

    //ItemViewHolder 클래스 선언
    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //아이템 뷰(list_item_note.xml)에 데이터 바인딩(noteTitle, noteImage)
        fun bindItems(board: BoardEntity) {
            /* 게시글 맵핑하기 */
            itemView.item_txt_content.text = board.boardContent
            itemView.item_txt_fee.text="구독료 : "+board.subFee
            itemView.item_txt_useage.text="지속 사용 여부 : " + board.usage
            itemView.item_txt_contents.text="컨텐츠 : " + board.subContents

            /* List 화면에서 아이템 뷰를 누르면 DetailFragment로 넘어감 */
            itemView.setOnClickListener {
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
