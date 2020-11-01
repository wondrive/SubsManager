package com.example.subsmanager2.board

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.subsmanager2.R
import kotlinx.android.synthetic.main.fragment_board_list.view.platformlist
import kotlinx.android.synthetic.main.fragment_platfrom_board.view.*


class platfromBoardFragment : Fragment() {
    //TODO : firestore과 통신이 너무 느림...

    //어댑터 생성
    var boardAdapter = PlatformBoardAdapter()
    var rootView:View?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //fragment_list 인플레이션
        rootView = inflater.inflate(R.layout.fragment_platfrom_board, container, false)
        //게시글 작성
        rootView?.fab_add_note?.setOnClickListener {
            findNavController().navigate(R.id.action_platfromBoardFragment_to_platformBoardWriteFragment)
        }
        return rootView// 생성한 fragment_list 뷰 반환
    }//end of onCreateView

    override fun onResume() {
        super.onResume()
        /* 어댑터 초기화*/
        Log.d("this is","platformboard")

        rootView?.platformlist?.adapter = boardAdapter
        rootView?.platformlist?.layoutManager = LinearLayoutManager(requireContext())
    }

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