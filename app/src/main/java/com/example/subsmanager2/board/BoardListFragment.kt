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
import kotlinx.android.synthetic.main.fragment_board_list.view.*
import kotlinx.android.synthetic.main.fragment_board_list.view.fab_add_note

/**
 * A simple [Fragment] subclass.
 */
class BoardListFragment : Fragment() {

    /* boardDao를 Lazy 키워드를 이용하여 처음 호출될때 초기화하도록 설정 */
    val boardDao by lazy { DatabaseModule.getDatabase(requireContext()).boardDao() }

    //어댑터 생성
    val boardAdapter = BoardAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //fragment_list 인플레이션
        val rootView = inflater.inflate(R.layout.fragment_board_list, container, false)

        /* 어댑터 초기화*/
        rootView.platformlist.adapter = boardAdapter
        rootView.platformlist.layoutManager = LinearLayoutManager(requireContext())

        rootView.fab_add_note.setOnClickListener {
            findNavController().navigate(R.id.action_boardListFragment_to_writeBoard)
        }

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
