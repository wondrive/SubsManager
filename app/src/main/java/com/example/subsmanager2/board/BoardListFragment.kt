package com.example.subsmanager2.board

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.subsmanager2.R
import com.example.subsmanager2.database.DatabaseModule
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_board_list.view.*

/**
 * A simple [Fragment] subclass.
 */
class BoardListFragment : Fragment() {

    /*
    * TODO : 로그인 여부 확인하기 [done]
    * TODO : 컨텐츠 게시판 작성 xml생성 [done]
    * TODO : 컨텐츠 게시판 작성 UI수정
    * TODO : 좋아요, 별점 기능 구현
    * */

    /* boardDao를 Lazy 키워드를 이용하여 처음 호출될때 초기화하도록 설정 */
    val boardDao by lazy { DatabaseModule.getDatabase(requireContext()).boardDao() }

    //어댑터 생성
    val boardAdapter = BoardAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //현재 로그인한 유저정보를 가져옴
        val user = FirebaseAuth.getInstance().currentUser
        var isLogin : Boolean =false

        if (user != null) {
            isLogin=true
        }

        // 로그인하지 않은 사용자는 게시판을 이용할 수 없음
        if(!isLogin){
            val rootView = inflater.inflate(R.layout.fragment_board_list_non_member, container, false)
            return rootView
        }

        //fragment_list 인플레이션
        val rootView = inflater.inflate(R.layout.fragment_board_list, container, false)

        /* 어댑터 초기화*/
        // platform adapter
        rootView.platformlist.adapter = boardAdapter
        rootView.platformlist.layoutManager = LinearLayoutManager(requireContext())

        //플랫폼 게시판 전체보기
        rootView.btn_show_all_platform.setOnClickListener{
            findNavController().navigate(R.id.action_boardListFragment_to_platfromBoardFragment)
        }
        //컨텐츠 게시판 전체보기
//        rootView.btn_show_all_contents.setOnClickListener{
//        }

        return rootView// 생성한 fragment_list 뷰 반환
    }//end of onCreateView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }//end of onViewCreated
    override fun onStart() {
        super.onStart()
    }
    override fun onDestroy() {
        super.onDestroy()
    }

}
