package com.example.subsmanager2.board

import android.content.ContentValues
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
import com.example.subsmanager2.entity.PlatformBoardEntity
import kotlinx.android.synthetic.main.list_item_platform_board.view.*

/*
firestore의 데이터 읽기 수행
 */
//class BoardAdapter(var boardList: List<BoardEntity> = emptyList())
class BoardAdapter : RecyclerView.Adapter<BoardAdapter.ItemViewHolder>() {

    val platformBoardDao = PlatformBoardDao()
    var platformBoardList = ArrayList<PlatformBoardEntity>()

    //TODO: 반환 아이템은 최대 3개[done]
    private var platformBoardItemCount = 0
    //getItemCount는 뭐하는 메소드임
    override fun getItemCount() = platformBoardList.size


    // 코드가 더럽네요 ^^;; 정신멀쩡할떄 한번 고쳐보겠습니다 ㄱ -;;
    init {
        // firebase에서 boardlist 불러온 뒤 Entity로 변환해 ArrayList에 담음
        platformBoardDao.db.collection("count")
            .document("platform_board_count")
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    platformBoardItemCount = document.get("count").toString().toInt()
                    // 반환 아이템은 최대 3개로 고정한다.
                    // 작성된 게시글이 3개 초과일 때
                    if(platformBoardItemCount > 2){
                        platformBoardDao.db.collection("platform_board")
                            // 최신순으로 데이터를 3개 가져옴
                            .orderBy("boardId", com.google.firebase.firestore.Query.Direction.DESCENDING)
                            .limit(3)
                            .get()
                            .addOnSuccessListener { result ->
                                platformBoardList.clear()
                                for (document in result) {
                                    val board = document.toObject(PlatformBoardEntity::class.java)
                                    platformBoardList?.add(board)
                                    Log.d("board : ",board.boardTitle)
                                }
                                notifyDataSetChanged()
                            }
                            .addOnFailureListener { exception ->
                                Log.w(TAG, "Error getting documents.", exception)
                            }
                    }
                    // 작성된 게시글이 3개 이하일 때
                    else {
                        platformBoardDao.db.collection("platform_board")
                            .get()
                            .addOnSuccessListener { result ->
                                platformBoardList.clear()
                                for (document in result) {
                                    val board = document.toObject(PlatformBoardEntity::class.java)
                                    platformBoardList?.add(board)
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
                Log.d(TAG, "get failed with ", exception)
            }
    }

    //platform board 리뷰한 구독 앱 사진 등록
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
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_platform_board, parent, false)

        /* 뷰를 데이터와 맵핑하기위해 생성한 뷰홀더를 반환*/
        return ItemViewHolder(itemView)
    }

    //뷰홀더에 데이터를 바인딩
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        //뷰홀더에 데이터를 바인딩하는 bindItems() 메서드 호출
        holder.bindItems(platformBoardList.get(position))
    }

    //ItemViewHolder 클래스 선언
    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(board: PlatformBoardEntity) {
            /* 게시글 맵핑하기 */
            itemView.item_txt_platform.text = board.boardTitle
            itemView.item_txt_content.text = board.boardContent
            itemView.item_txt_fee.text="구독료 : "+board.subFee
            itemView.item_txt_useage.text="지속 사용 여부 : " +board.usage
            itemView.item_txt_contents.text="컨텐츠 : " +board.subContents
            itemView.item_txt_rating.text="평가 : "+board.ratingScore
            setSubAppIcon(board.boardTitle,itemView.img_sub_app)

            /* List 화면에서 아이템 뷰를 누르면 DetailFragment로 넘어감 */
            itemView.setOnClickListener {
                Navigation.findNavController(itemView).navigate(
                    //platform detail로 이동
                    R.id.action_boardListFragment_to_fragmentPlatformBoardDetail,
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
