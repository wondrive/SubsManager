package com.example.subsmanager2.board

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.subsmanager2.R
import com.example.subsmanager2.database.DatabaseModule
import kotlinx.android.synthetic.main.fragment_contents_board.view.*

class contentsBoardFragment : Fragment() {

    //어댑터 생성
    val boardAdapter = ContentsBoardAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //fragment_list 인플레이션
        val rootView = inflater.inflate(R.layout.fragment_contents_board, container, false)

        //글 작성
        rootView.fab_add_note.setOnClickListener {
            findNavController().navigate(R.id.action_contentsBoardFragment_to_contentsBoardWriteFragment)
        }

        /* 어댑터 초기화*/
        rootView.contentslist.adapter = boardAdapter
        rootView.contentslist.layoutManager = LinearLayoutManager(requireContext())

        return rootView// 생성한 fragment_list 뷰 반환
    }//end of onCreateView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
