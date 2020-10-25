package com.example.subsmanager2.recommend

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.subsmanager2.R
import com.example.subsmanager2.dao.RecommendDao
import com.example.subsmanager2.database.DatabaseModule
import kotlinx.android.synthetic.main.fragment_agric_search.view.*
import kotlinx.android.synthetic.main.fragment_recommend_list.view.*
import kotlinx.android.synthetic.main.fragment_recommend_list.view.btn_search
import kotlinx.android.synthetic.main.fragment_recommend_list.view.progress_loader

/**
 * A simple [Fragment] subclass.
 */
class RecommendListFragment : Fragment() {

    val TAG = "RecommendListFragment"
    val recommendDao = RecommendDao()
    val recommendAdapter = RecommendAdapter()   //어댑터 생성

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var rootView = inflater.inflate(R.layout.fragment_recommend_list, container, false)

        /* 어댑터 초기화*/
        rootView.list_recommend.adapter = recommendAdapter
        rootView.list_recommend.layoutManager = LinearLayoutManager(requireContext())

        //recommendAdapter.recommendList = recommendDao.selectRecommendList("영화/드라마")

        return rootView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // 검색 버튼 클릭시
        view.btn_search.setOnClickListener {
            val selectCategory = view.spinner_category.selectedItem.toString()
            Log.i(TAG, "검색조건 : "+selectCategory)


            // Dao에서 리스트 뽑아 바인딩
            //recommendAdapter.recommendList = recommendDao.selectRecommendList(selectCategory = selectCategory)
            //Log.i(TAG, "검색결과 : "+recommendAdapter.recommendList)
            //recommendAdapter.notifyDataSetChanged()

            /* 검색 완료 후 로딩바 사라지기 . */
            //view.progress_loader.visibility = View.GONE
            // 검색조건이 하나라도 있으면
            /*if (!selectCategory.equals("")) {

            } else {
                Toast.makeText(requireContext(), "검색조건을 입력해주세요.", Toast.LENGTH_SHORT).show()
            }*/
        }
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}
