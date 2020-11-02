package com.example.subsmanager2.subs

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.subsmanager2.R
import com.example.subsmanager2.dao.SubsDao
import com.example.subsmanager2.database.DatabaseModule
import com.example.subsmanager2.entity.SubsEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_subs_detail.*
import kotlinx.android.synthetic.main.fragment_subs_detail.view.*
import kotlinx.android.synthetic.main.fragment_subs_detail.view.txt_subs_name
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 */
class SubsDetailFragment : Fragment() {

    val TAG = "SubsDetailFragment"

    //dao 참조
    private val subsDao by lazy { SubsDao() }

    private var subsId:Long =0
    private var userId:String=""
    private var updateYn:String=""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_subs_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // arguments에서 SUBS_ID 추출
        subsId = arguments?.getLong("SUBS_ID") ?: kotlin.run { throw Error("SUBS_ID가 없습니다.") }
        userId = arguments?.getString("USER_ID") ?: kotlin.run { throw Error("USER_ID가 없습니다.") }
        updateYn = arguments?.getString("UPDATE_YN")?:kotlin.run { throw  Error("Update_YN이 없습니다.") }

        // 본인 게시글일때만 수정, 삭제 버튼 보이게
        FirebaseAuth.getInstance().currentUser?.let {
            if(it.email.equals(userId)) {
                view.btn_detail_edit.visibility = View.VISIBLE
                view.btn_detail_delete.visibility = View.VISIBLE
            } else {
                view.btn_detail_edit.visibility = View.GONE
                view.btn_detail_delete.visibility = View.GONE
            }
        }

        /* DetailFragment에서 LiveData observe
           - DB에서 트랜잭션(Transaction)이 발생하면 UI를 갱신
           - (박진아 작성: 음.. LiveData observe에 대한 개념이 없어서 바로 firestore과 연결하는 것으로 구현했습니다☔︎
        */
//        subsDao.selectLiveSubs(subsId)?.observe(viewLifecycleOwner, Observer {
//            view.txt_subs_name.text=it.subsName
//            Log.d(TAG, "Subs_Detail 출력 ::: " + it.subsName)
//        })

        //데이터 파싱
        val firestore by lazy { FirebaseFirestore.getInstance() }
        var subs = SubsEntity()
        firestore.collection("subs").whereEqualTo("subsId", subsId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                for(document in documentSnapshot!!) {
                    Log.d(TAG, "Subs_Detail  ::: " + document.data)
                    subs = document.toObject(SubsEntity::class.java)
                    view.txt_subs_name.text=subs.subsName
                    view.txt_charge_date.text=subs.feeDate
                    view.txt_fee.text=subs.fee
                    view.txt_alarm_d_day.text=subs.alarmDday
                    view.txt_usage.text="미정"
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Subs 상세 읽기 실패: ", exception)
            }

        // 데이터 수정 -- 작업중입니다.^^
        view.btn_detail_edit.setOnClickListener {
            //childFragmentManager:  Fragment에서 다른 Fragment를 만들 때(DetailFragment에서 DialFragment 만들 때)
            //RecipeWriteFragment().show(childFragmentManager, recipeId.toString())
            findNavController().navigate(R.id.action_subsDetailFragment_to_fragmentSubsUpdate,
                Bundle().apply {
                    putLong("SUBS_ID", subsId!!)
                    putString("UPDATE_YN",updateYn!!)
                    putString("USER_ID",userId!!)
                })
        }

        // 데이터 삭제
        view.btn_detail_delete.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO){
                subsDao.deleteSubs(subsId)
            }
            Toast.makeText(requireContext(), "삭제되었습니다.", Toast.LENGTH_SHORT).show()
            //뒤로가기
            findNavController().popBackStack()
        }
    }//end of onViewCreated

}
