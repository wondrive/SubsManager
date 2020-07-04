package com.example.homecookhelper.agric

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.homecookhelper.R
import com.example.homecookhelper.database.DatabaseModule
import com.example.homecookhelper.entity.AgricEntity
import kotlinx.android.synthetic.main.fragment_agric_detail.view.*
import kotlinx.android.synthetic.main.fragment_agric_detail.view.txt_agric_name
import kotlinx.android.synthetic.main.list_item_agric.view.*
import java.io.BufferedInputStream
import java.io.IOException
import java.net.URL
import java.net.URLConnection

class AgricDetailFragment : Fragment() {

    //Dao 참조
    private val dao by lazy { DatabaseModule.getDatabase(requireContext()).agricDao() }

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
        val agricId =
            arguments?.getString("AGRIC_ID") ?: kotlin.run { throw Error("AGRIC_ID가 없습니다.") }

        dao.selectAgric(agricId).observe(viewLifecycleOwner, Observer { agric: AgricEntity ->
            view.txt_agric_name.setText("테스트 이름!!")
            view.txt_origin.setText(agric.agricName)
            view.txt_month.setText(agric.agricName)
            view.txt_effect.setText(agric.agricName)
            view.txt_cook_method.setText(agric.agricName)
            view.txt_puchase_method.setText(agric.agricName)
            view.txt_treat_method.setText(agric.agricName)
        })
    }
}
