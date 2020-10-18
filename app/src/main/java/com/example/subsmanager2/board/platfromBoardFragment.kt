package com.example.subsmanager2.board

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.subsmanager2.R
import com.example.subsmanager2.database.DatabaseModule
import com.example.subsmanager2.entity.BoardEntity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import jdk.nashorn.internal.parser.JSONParser
import kotlinx.android.synthetic.main.fragment_board_list.view.*
import org.json.JSONObject


class platfromBoardFragment : Fragment() {
    //TODO : firebase get[done]
    //TODO : data--> list<Entity> 하아........ arraylist로 할까나 ...

    /* 파이어베이스 데이터베이스 */
    private var database: DatabaseReference = Firebase.database.reference
    val platformBoardList = database.child("platform_board")

    /* 게시글 저장할 boardlist*/
    var boardlist: ArrayList<BoardEntity>? = ArrayList()

    /* boardDao를 Lazy 키워드를 이용하여 처음 호출될때 초기화하도록 설정 */
    val boardDao by lazy { DatabaseModule.getDatabase(requireContext()).boardDao() }

    //어댑터 생성
    val boardAdapter = PlatformBoardAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //fragment_list 인플레이션
        val rootView = inflater.inflate(R.layout.fragment_platfrom_board, container, false)

        rootView.fab_add_note.setOnClickListener {
            findNavController().navigate(R.id.action_platfromBoardFragment_to_writeBoard2)
        }

        platformBoardList.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", error.toException())
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                /*TODO: parsing...*/
                var gson = Gson()
                var jsonString = gson.toJson(TestModel(1,"Test"))
                Assert.assertEquals(jsonString, """{"id":1,"description":"Test"}""")

                for (platformlist in dataSnapshot.children) {

                    val data = JSONObject(platformlist.value.toString())
                    val boardTitle =data.get("boardTitle")
                    Log.d("boardTitle : ",boardTitle.toString())
//                    boardlist?.add(platformlist.value as BoardEntity)
//                    Log.d("boardlist: ", boardlist?.get(index).toString())
//                    index++
                }
            }
        })

        /* 어댑터 초기화*/
        rootView.platformlist.adapter = boardAdapter
        rootView.platformlist.layoutManager = LinearLayoutManager(requireContext())

        return rootView// 생성한 fragment_list 뷰 반환
    }//end of onCreateView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        boardDao.selectBoard().observe(viewLifecycleOwner, androidx.lifecycle.Observer{
            boardAdapter.boardList = it//어댑터에 변경된 note 전달
            boardAdapter.notifyDataSetChanged()//어댑터에 변경 공지
        })
    }//end of onViewCreated
    override fun onStart() {
        super.onStart()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}

