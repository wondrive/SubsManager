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
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import com.example.subsmanager2.R
import com.example.subsmanager2.dao.PlatformBoardDao
import com.example.subsmanager2.entity.PlatformBoardEntity
import com.example.subsmanager2.entity.SubsEntity
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.list_item_platform_board.view.*

//class PlatformBoardAdapter(var boardList: List<BoardEntity> = emptyList()) :
class PlatformBoardAdapter : RecyclerView.Adapter<PlatformBoardAdapter.ItemViewHolder>() {

    val boardDao = PlatformBoardDao()
    var boardList = ArrayList<PlatformBoardEntity>()

    override fun getItemCount() = boardList.size

    init {
        boardDao.db.collection("platform_board")
            // 최신순으로 데이터를 가져옴
            .orderBy("boardId", Query.Direction.DESCENDING)
            .addSnapshotListener(){ querySnapshot, firebaseFirestoreException ->
                // ArrayList 비워줌
                boardList.clear()
                for (snapshot in querySnapshot!!.documents) {
                    val board = snapshot.toObject(PlatformBoardEntity::class.java)
                    if (board != null) {
                        boardList?.add(board)
                    }
                }
                notifyDataSetChanged()
            }



//            .get()
//            .addOnSuccessListener { result ->
//                boardList.clear()
//                for (document in result) {
//                    val board = document.toObject(PlatformBoardEntity::class.java)
//                    boardList?.add(board)
//                }
//                notifyDataSetChanged()
//            }
//            .addOnFailureListener { exception ->
//                Log.w(TAG, "Error getting documents.", exception)
//            }
    }

    //리뷰한 구독 앱 사진 등록
    fun setSubAppIcon(appName: String?, img_sub_app: ImageView) {
        when (appName) {
            "넷플릭스" -> img_sub_app.setImageResource(R.drawable.netflix)
            "왓챠" -> img_sub_app.setImageResource(R.drawable.watcha)
            "유튜브 프리미엄" -> img_sub_app.setImageResource(R.drawable.youtube)
        }
    }

    /*뷰홀더 생성하여 반환*/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_platform_board, parent, false)

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
        fun bindItems(board: PlatformBoardEntity) {
            /* 게시글 맵핑하기 */
            itemView.item_txt_platform.text = board.boardTitle
            itemView.item_txt_content.text = board.boardContent
            itemView.item_txt_fee.text = "구독료 : " + board.subFee
            itemView.item_txt_useage.text = "지속 사용 여부 : " + board.usage
            itemView.item_txt_contents.text = "컨텐츠 : " + board.subContents
            itemView.item_txt_rating.text = "평가 : " + board.ratingScore
            setSubAppIcon(board.boardTitle, itemView.img_sub_app)

            /* List 화면에서 아이템 뷰를 누르면 DetailFragment로 넘어감 */
            itemView.setOnClickListener {
                // 클릭한 아이템의 인덱스
                var index = adapterPosition
                if (index != NO_POSITION) {

                    Navigation.findNavController(itemView).navigate(
                        R.id.action_platfromBoardFragment_to_fragmentPlatformBoardDetail,
                        //아 ~ 테스형 ~ 정답은 가까이에 있었군요 ~ 승원씨가 작성한 agricAdapter가 Bundle로 id값 넘기는걸 이제 봤어 테스형 ~
                        Bundle().apply {
                            putString("boardId", board.boardId.toString()!!)
                        })
                }
            }//end of setOnClickListener
        }//end of bindItems
    }//end of ItemViewHold
}