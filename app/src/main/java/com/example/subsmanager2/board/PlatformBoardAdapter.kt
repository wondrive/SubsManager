package com.example.subsmanager2.board

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.subsmanager2.R
import com.example.subsmanager2.dao.PlatformBoardDao
import com.example.subsmanager2.entity.BoardEntity
import com.example.subsmanager2.entity.SubsEntity
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.list_item_board.view.*

//class PlatformBoardAdapter(var boardList: List<BoardEntity> = emptyList()) :
class PlatformBoardAdapter : RecyclerView.Adapter<PlatformBoardAdapter.ItemViewHolder>() {

    val boardDao = PlatformBoardDao()
    var boardList = ArrayList<BoardEntity>()

    override fun getItemCount() = boardList.size
    var debug = printDebug()
    fun printDebug(){
        Log.d("debug",boardList.size.toString())
    }

    init {
        boardDao. db.collection("platform_board")
                // 최신순으로 데이터를 가져옴
            .orderBy("boardId",Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                boardList.clear()
                for (document in result) {
                    val board = document.toObject(BoardEntity::class.java)
                    boardList?.add(board)
                    Log.d("board : ",board.boardTitle)
                }
                notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }

    //리뷰한 구독 앱 사진 등록
    fun setSubAppIcon(appName:String?, img_sub_app: ImageView) {
        when (appName){
            "넷플릭스" ->img_sub_app.setImageResource(R.drawable.netflix)
            "왓챠" ->img_sub_app.setImageResource(R.drawable.watcha)
            "유튜브 프리미엄" ->img_sub_app.setImageResource(R.drawable.youtube)
        }
    }

    /*뷰홀더 생성하여 반환*/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_board, parent, false)

        /* 뷰를 데이터와 맵핑하기위해 생성한 뷰홀더를 반환*/
        return ItemViewHolder(itemView)
    }

    //뷰홀더에 데이터를 바인딩
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        //뷰홀더에 데이터를 바인딩하는 bindItems() 메서드 호출
        holder.bindItems(boardList.get(position))
    }

    //ItemViewHolder 클래스 선언
    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //아이템 뷰(list_item_note.xml)에 데이터 바인딩(noteTitle, noteImage)
        fun bindItems(board: BoardEntity) {
            /* 게시글 맵핑하기 */
            itemView.item_txt_title.text = board.boardTitle
            itemView.item_txt_content.text = board.boardContent
            itemView.item_txt_fee.text="구독료 : "+board.subFee
            itemView.item_txt_useage.text="지속 사용 여부 : " +board.usage
            itemView.item_txt_contents.text="컨텐츠 : " +board.subContents
            itemView.item_rating_score.text="평가 : "+board.ratingScore
            setSubAppIcon(board.boardTitle,itemView.img_sub_app)


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
