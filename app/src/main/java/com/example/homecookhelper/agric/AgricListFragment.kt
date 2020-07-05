package com.example.homecookhelper.agric

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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Database
import com.example.homecookhelper.R
import com.example.homecookhelper.database.DatabaseModule
import kotlinx.android.synthetic.main.fragment_agric_list.view.*
import java.util.*

class AgricListFragment : Fragment() {

    /* 데이터베이스를 가져옵니다.*/
    val database by lazy {
        DatabaseModule.getDatabase(requireContext())
    }

    //resultViewModel 참조
    val resultViewModel by lazy {
        ViewModelProvider(requireActivity()).get(AgricViewModel::class.java)
    }

    //데이터 가져온 후 화면 생성 위한 어댑터 생성
    val agricAdapter = AgricAdapter()

    //-----------------------------------------
    /* noteDao를 Lazy 키워드를 이용하여 처음 호출될때 초기화하도록 설정 */
    //val agricDao by lazy { DatabaseModule.getDatabase(requireContext()).agricDao() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //fragment_list 인플레이션
        val rootView = inflater.inflate(R.layout.fragment_agric_list, container, false)

        /* 어댑터 초기화*/
        /*rootView.list_agric.adapter = agricAdapter
        rootView.list_agric.layoutManager = LinearLayoutManager(requireContext())*/

        return rootView// 생성한 fragment_list 뷰 반환
    }

    @SuppressLint("RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //todo5 네트워크 call을 수행 - 서버에 검색 요청

        /* SearchFragment에 전달한 arguments에서 검색 요청에 필요한
           SELECT_FRUIT, SELECT_DATE, RESULT_AMOUNT 추출 */
        //val selectAgric = arguments?.getString("SELECT_AGRIC")
        //val selectMonth = arguments?.getString("SELECT_MONTH)

        //경락가격정보 서버에 검색 요청(resultViewModel.loadDataFromURL() 함수 호출)
        //if (selectDate != null && selectFruit != null && resultAmount != null) {
        /* 경락가격정보 서버에 검색 요청
           - resultViewModel.loadDataFromURL() 함수 호출
        */
        Calendar.getInstance().apply { time = Date(System.currentTimeMillis()) }
        resultViewModel.loadDataFromURL(selectAgric = "", selectMonth = Calendar.MONTH.toString()+"월")

        //서버에서 응답한 응답 데이터의 변화를 감지하기 위해 LiveData(resultList())에 observe 설정
        resultViewModel.resultList().observe(viewLifecycleOwner, Observer {
            /* resultAdpater에 데이터에 변동됨을 알려줍니다. */
            agricAdapter.agricList = it//검색한  List<FreshData>를  resultAdpater에 전달
            Log.i("AGRIC", "it: $it")
            agricAdapter.notifyDataSetChanged()

            /* 로딩은 사라집니다. */
            view.progress_loader.visibility = View.GONE
            /* 저장버튼을 보여줍니다. */
            //view.floting_save.visibility = View.VISIBLE
        })//end of observe

        /*  리사이클러뷰에 구분선 설정 */
        view.list_agric.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        );

        /* 리사이클러뷰에 어댑터 및 레이아웃메니저 설정 */
        view.list_agric.adapter = agricAdapter
        view.list_agric.layoutManager = LinearLayoutManager(requireContext())

    }//end of if
}//end of onViewCreated

