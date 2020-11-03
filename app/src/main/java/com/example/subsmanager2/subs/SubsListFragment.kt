package com.example.subsmanager2.subs

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.subsmanager2.R
import com.example.subsmanager2.dao.SubsDao
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.kftc.openbankingsample2.biz.main.OpenBankingMainActivity
import kotlinx.android.synthetic.main.fragment_subs_list.*
import kotlinx.android.synthetic.main.fragment_subs_list.view.*

/**
 * A simple [Fragment] subclass.
 */
class SubsListFragment : Fragment() {

    //현재 로그인한 유저정보를 가져옴
    var user =""
    //지출하고 있는 총 구독료
    var totalFee = 0

    // DAO
    val subsDao = SubsDao()

    //현재 view
    var rootView:View?=null

    //어댑터 생성
    val subsAdapter = SubsAdapter()

    var isFabOpen = false;
    private var fab_open: Animation? = null;
    private var fab_close: Animation? = null;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_subs_list, container, false)

        /* 어댑터 초기화*/
        //rootView.list_subs.adapter = subsAdapter
        //rootView.list_subs.layoutManager = LinearLayoutManager(requireContext())
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

        /* Fab 애니메이션 초기화 */
        fab_open = AnimationUtils.loadAnimation(context, R.anim.fab_open)
        fab_close = AnimationUtils.loadAnimation(context, R.anim.fab_close)

        /* 구독앱 추가 버튼 누르면 */
        rootView?.fab_main?.setOnClickListener {
            toggleFab();
        }
        // 자동 등록 버튼 (은행연동, OpenBanking)
        rootView?.fab_register_openbanking?.setOnClickListener {
            //findNavController().navigate(R.id.action_subsListFragment_to_subsRegisterOpenBankingFragment)
            var intent = Intent(activity, OpenBankingMainActivity::class.java)
            startActivity(intent)
        }
        rootView?.fab_register?.setOnClickListener {
            findNavController().navigate(R.id.action_subsListFragment_to_subsRegisterFragment)
        }
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 여기 주석    @혜린 @진아
        /*var id: String = ""
        FirebaseAuth.getInstance().currentUser?.let {
            //id = it.email?:
        }

        subsDao.selectSubsList(id).observe(viewLifecycleOwner, androidx.lifecycle.Observer{
            subsAdapter.subsList = it//어댑터에 변경된 note 전달
            subsAdapter.notifyDataSetChanged()//어댑터에 변경 공지
        })*/

    }//end of onViewCreated

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        /* 어댑터 초기화*/
        rootView?.list_subs?.adapter = subsAdapter
        rootView?.list_subs?.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onStop() {
        super.onStop()
        //requireActivity().finish()
        //activity!!.finish()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun toggleFab() {
        if(isFabOpen) { // 열려있었음->닫힐때
            fab_main.setBackgroundColor(resources.getColor(R.color.light_grey))
            fab_register.startAnimation(fab_close)
            fab_register_openbanking.startAnimation(fab_close)
            fab_register.setClickable(false)
            fab_register_openbanking.setClickable(false)
            isFabOpen = false
        }
        else {  // 닫혀있었음 -> 열릴때
            fab_main.setBackgroundColor(resources.getColor(R.color.white))
            fab_register.startAnimation(fab_open)
            fab_register_openbanking.startAnimation(fab_open)
            fab_register.setClickable(true)
            fab_register_openbanking.setClickable(true)
            isFabOpen = true
        }
    }



}
