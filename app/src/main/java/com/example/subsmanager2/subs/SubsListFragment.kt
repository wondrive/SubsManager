package com.example.subsmanager2.subs

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.subsmanager2.R
import com.example.subsmanager2.dao.SubsDao
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_mypage.view.*
import kotlinx.android.synthetic.main.fragment_subs_list.view.*

/**
 * A simple [Fragment] subclass.
 */
class SubsListFragment : Fragment() {

    //현재 로그인한 유저정보를 가져옴
    var user =""
    //지출하고 있는 총 구독료
    var totalFee = 0
    //어댑터 생성
    val subsAdapter = SubsAdapter()
    //현재 view
    var rootView:View?=null
    // DAO
    val subsDao = SubsDao()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //fragment_list 인플레이션
        rootView = inflater.inflate(R.layout.fragment_subs_list, container, false)
        if(subsAdapter != null) {
            rootView?.txt_notice?.visibility = View.GONE
        }

        //현재 사용자 정보
        FirebaseAuth.getInstance().currentUser?.let { it ->
            user=it.email.toString()
            Log.d("user adapter:",user)
        }

        //요금 총합
        totalFee=0
            subsDao.firestore?.collection("subs")
                .orderBy("subsId", Query.Direction.DESCENDING)
                .whereEqualTo("userId", user.toString())
                ?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    for (snapshot in querySnapshot!!.documents) {
                        Log.d("fee : ", snapshot.get("fee").toString())
                        val fee = snapshot.get("fee")
                        if (fee is String) {
                            totalFee += fee.toInt()
                        }
                    }
                    rootView?.item_txt_total_fee?.text = "총 구독료: " + totalFee.toString() + "₩"
                }


        /* 구독앱 추가 버튼 누르면 */
        rootView?.fab_add_note?.setOnClickListener {
            findNavController().navigate(R.id.action_subsListFragment_to_subsWriteFragment)
        }

        return rootView// 생성한 fragment_list 뷰 반환
    }//end of onCreateView

    override fun onResume() {
        super.onResume()
        /* 어댑터 초기화*/
        rootView?.list_subs?.adapter = subsAdapter
        rootView?.list_subs?.layoutManager = LinearLayoutManager(requireContext())

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
