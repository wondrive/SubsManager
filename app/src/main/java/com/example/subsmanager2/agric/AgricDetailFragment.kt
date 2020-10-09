package com.example.subsmanager2.agric

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.subsmanager2.R
import kotlinx.android.synthetic.main.fragment_agric_detail.view.*
import kotlinx.android.synthetic.main.fragment_agric_detail.view.txt_agric_name

class AgricDetailFragment : Fragment() {

    //데이터 가져온 후 화면 생성 위한 어댑터 생성
    val agricAdapter = AgricAdapter()

    //resultViewModel 참조
    val resultViewModel by lazy {
        ViewModelProvider(requireActivity()).get(AgricViewModel::class.java)
    }

    /*//Dao 참조
    private val dao by lazy { DatabaseModule.getDatabase(requireContext()).agricDao() }*/

    //fragment_detail 뷰를 생성(인플레이션)하여 반환
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_agric_detail, container, false)
    }//end of onCreateView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /* arguments에서 ID 추출 */
        val agricName = arguments?.getString("AGRIC_NAME") ?: kotlin.run { throw Error("AGRIC_NAME이 없습니다.") }

        resultViewModel.loadDetailFromURL(selectAgric = agricName)

        //서버에서 응답한 응답 데이터의 변화를 감지하기 위해 LiveData(resultList())에 observe 설정
        resultViewModel.resultDetail().observe(viewLifecycleOwner, Observer {
            //resultAdpater에 데이터에 변동됨을 알려줍니다.
            agricAdapter.agricDetail = it//검색한  List<FreshData>를  resultAdpater에 전달
            Log.i("AGRIC", "it: $it")
            agricAdapter.notifyDataSetChanged()

            view.txt_agric_name.setText(it.agricName)
            view.txt_origin.setText(it.origin)
            view.txt_month.setText(it.month)
            view.txt_effect.setText(it.effect)
            view.txt_cook_method.setText(it.cookMethod)
            view.txt_puchase_method.setText(it.purchaseMethod)
            view.txt_treat_method.setText(it.treatMehtod)

        })//end of observe

    }
}