package com.example.subsmanager2.board

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.subsmanager2.R
import com.example.subsmanager2.dao.ContentsBoardDao
import com.example.subsmanager2.entity.ContentsBoardEntity
import kotlinx.android.synthetic.main.list_item_contents_board.view.*


class BoardAdapter2 : RecyclerView.Adapter<BoardAdapter2.ItemViewHolder>() {

    val contentsBoardDao = ContentsBoardDao()
    var contentsBoardList = ArrayList<ContentsBoardEntity>()

    //TODO: 반환 아이템은 최대 3개[done]
    private var contentsBoardItemCount = 0
    //getItemCount는 뭐하는 메소드임
    override fun getItemCount() = contentsBoardList.size

    init {
        // firebase에서 boardlist 불러온 뒤 Entity로 변환해 ArrayList에 담음
        contentsBoardDao.db.collection("count")
            .document("contents_board_count")
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    contentsBoardItemCount = document.get("count").toString().toInt()
                    // 반환 아이템은 최대 3개로 고정한다.
                    // 작성된 게시글이 3개 초과일 때
                    if(contentsBoardItemCount > 2){
                        contentsBoardDao.db.collection("contents_board")
                            // 최신순으로 데이터를 3개 가져옴
                            .orderBy("boardId", com.google.firebase.firestore.Query.Direction.DESCENDING)
                            .limit(3)
                            .get()
                            .addOnSuccessListener { result ->
                                contentsBoardList.clear()
                                for (document in result) {
                                    val board = document.toObject(ContentsBoardEntity::class.java)
                                    contentsBoardList?.add(board)
                                    Log.d("board : ",board.boardTitle)
                                }
                                notifyDataSetChanged()
                            }
                            .addOnFailureListener { exception ->
                                Log.w(ContentValues.TAG, "Error getting documents.", exception)
                            }
                    }
                    // 작성된 게시글이 3개 이하일 때
                    else {
                        contentsBoardDao.db.collection("contents_board")
                            .get()
                            .addOnSuccessListener { result ->
                                contentsBoardList.clear()
                                for (document in result) {
                                    val board = document.toObject(ContentsBoardEntity::class.java)
                                    contentsBoardList?.add(board)
                                    Log.d("not board id: ", board.boardId.toString())
                                }
                                notifyDataSetChanged()
                            }
                            .addOnFailureListener { exception ->
                                Log.w(ContentValues.TAG, "Error getting documents.", exception)
                            }
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "get failed with ", exception)
            }
    }

    fun setSubAppIcon(appName:String?, img_contents: ImageView) {
        when (appName) {
            "넷플릭스" -> img_contents.setImageResource(R.drawable.netflix)
            "왓챠" -> img_contents.setImageResource(R.drawable.watcha)
            "유튜브 프리미엄" -> img_contents.setImageResource(R.drawable.youtube)
        }

    }
    /*뷰홀더 생성하여 반환*/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_contents_board, parent, false)

        /* 뷰를 데이터와 맵핑하기위해 생성한 뷰홀더를 반환*/
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        //뷰홀더에 데이터를 바인딩하는 bindItems() 메서드 호출
        holder.bindItems(contentsBoardList.get(position))
    }

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
                Navigation.findNavController(itemView).navigate(
                    R.id.action_boardListFragment_to_fragmentContentsBoardDetail,
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