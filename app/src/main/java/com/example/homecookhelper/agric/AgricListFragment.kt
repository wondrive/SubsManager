package com.example.homecookhelper.agric

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.homecookhelper.R
import com.example.homecookhelper.database.DatabaseModule
import kotlinx.android.synthetic.main.fragment_agric_list.view.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class AgricListFragment : Fragment() {

    /* noteDao를 Lazy 키워드를 이용하여 처음 호출될때 초기화하도록 설정 */
    val agricDao by lazy { DatabaseModule.getDatabase(requireContext()).agricDao() }

    //어댑터 생성
    val agricAdapter = AgricAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //fragment_list 인플레이션
        val rootView = inflater.inflate(R.layout.fragment_agric_list, container, false)

        /* 어댑터 초기화*/
        rootView.list_agric.adapter = agricAdapter
        rootView.list_agric.layoutManager = LinearLayoutManager(requireContext())

        return rootView// 생성한 fragment_list 뷰 반환
    }
}
