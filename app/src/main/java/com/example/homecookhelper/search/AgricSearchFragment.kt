package com.example.homecookhelper.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.homecookhelper.R
import com.example.homecookhelper.agric.AgricViewModel
import kotlinx.android.synthetic.main.fragment_agric_search.view.*
import kotlinx.android.synthetic.main.fragment_agric_search.view.btn_search
import kotlinx.android.synthetic.main.fragment_agric_search.view.list_agric
import kotlinx.android.synthetic.main.fragment_agric_search.view.progress_loader


class AgricSearchFragment : Fragment() {

    //resultViewModel 참조
    val resultViewModel by lazy {
        ViewModelProvider(requireActivity()).get(AgricViewModel::class.java)
    }

    //데이터 가져온 후 화면 생성 위한 어댑터 생성
    val agricAdapter = AgricSearchAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //fragment_list 인플레이션
        val rootView = inflater.inflate(R.layout.fragment_agric_search, container, false)
        return rootView// 생성한 fragment_list 뷰 반환
    }

    @SuppressLint("RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.btn_search.setOnClickListener {
            val selectAgric = view.edit_argric_name.text.toString()
            val selectMonth = view.spinner_month.selectedItem.toString()
            Log.i("검색조건 : ", selectAgric+", "+selectMonth)

            // month나 농산물 검색조건이 하나라도 있으면
            if( !selectAgric.equals("") || !selectMonth.equals("") ) {
                /*  서버에 검색 요청 resultViewModel.loadDataFromURL() 함수 호출*/
                resultViewModel.loadDataFromURL(selectAgric = selectAgric, selectMonth = selectMonth)

                //서버에서 응답한 응답 데이터의 변화를 감지하기 위해 LiveData(resultList())에 observe 설정
                resultViewModel.resultList().observe(viewLifecycleOwner, Observer {
                    /* resultAdpater에 데이터에 변동됨을 알려줍니다. */
                    agricAdapter.agricList = it//검색한  List<FreshData>를  resultAdpater에 전달
                    Log.i("AGRIC", "it: $it")
                    agricAdapter.notifyDataSetChanged()

                    /* 로딩은 사라집니다. */
                    view.progress_loader.visibility = View.GONE
                })//end of observe

                /* 리사이클러뷰에 어댑터 및 레이아웃메니저 설정 */
                view.list_agric.adapter = agricAdapter
                view.list_agric.layoutManager = LinearLayoutManager(requireContext())
            } else {
                Toast.makeText(requireContext(), "검색조건을 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

    } //end of onViewCreated

}