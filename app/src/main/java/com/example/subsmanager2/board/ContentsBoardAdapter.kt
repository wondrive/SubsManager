package com.example.subsmanager2.board

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil.DiffResult.NO_POSITION
import androidx.recyclerview.widget.RecyclerView
import com.example.subsmanager2.R
import com.example.subsmanager2.dao.ContentsBoardDao
import com.example.subsmanager2.entity.ContentsBoardEntity
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.list_item_contents_board.view.*

//class PlatformBoardAdapter(var boardList: List<BoardEntity> = emptyList()) :
class ContentsBoardAdapter : RecyclerView.Adapter<ContentsBoardAdapter.ItemViewHolder>() {

    val conboardDao = ContentsBoardDao()
    var conboardList = ArrayList<ContentsBoardEntity>()

    override fun getItemCount() = conboardList.size
    var debug = printDebug()
    fun printDebug(){
        Log.d("debug",conboardList.size.toString())
    }

    init {
        conboardDao. db.collection("contents_board")
            // 최신순으로 데이터를 가져옴
            .orderBy("boardId",Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                conboardList.clear()
                for (document in result) {
                    val board = document.toObject(ContentsBoardEntity::class.java)
                    conboardList?.add(board)
                    Log.d("board : ",board.boardTitle)
                }
                notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }

    //리뷰한 구독 앱 사진 등록
    fun setSubAppIcon(appName:String?, img_contents: ImageView) {
        when (appName){
            "넷플릭스" ->img_contents.setImageResource(R.drawable.netflix)
            "왓챠" ->img_contents.setImageResource(R.drawable.watcha)
            "유튜브 프리미엄" ->img_contents.setImageResource(R.drawable.youtube)
        }
    }

    /*뷰홀더 생성하여 반환*/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_contents_board, parent, false)

        /* 뷰를 데이터와 맵핑하기위해 생성한 뷰홀더를 반환*/
        return ItemViewHolder(itemView)
    }

    //뷰홀더에 데이터를 바인딩
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        //뷰홀더에 데이터를 바인딩하는 bindItems() 메서드 호출
        holder.bindItems(conboardList.get(position))
    }

    //ItemViewHolder 클래스 선언
    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //아이템 뷰(list_item_note.xml)에 데이터 바인딩(noteTitle, noteImage)
        fun bindItems(board: ContentsBoardEntity) {
            /* 게시글 맵핑하기 */
            itemView.content_txt_title.text = board.boardTitle
            itemView.content_txt_content.text = board.boardContent
            itemView.content_txt_story.text="스토리 : "+board.contentsStory
            itemView.content_txt_restart.text="재시청 여부 : " +board.contentsRestart
            itemView.content_txt_act.text="연기력 : " +board.contentsAct
            itemView.content_txt_rating.text="평가 : "+board.ratingScore
            setSubAppIcon(board.boardTitle,itemView.img_sub_app)

            /* List 화면에서 아이템 뷰를 누르면 DetailFragment로 넘어감 */
            itemView.setOnClickListener {

                var index = adapterPosition
                if(index != NO_POSITION) {
                    Navigation.findNavController(itemView).navigate(
                        R.id.action_contentsBoardFragment_to_fragmentContentsBoardDetail,
                        Bundle().apply{
                            putString("boardId", board.boardId.toString()!!)
                        })
                }
            }//end of setOnClickListener
        }//end of bindItems
    }//end of ItemViewHolder
}
