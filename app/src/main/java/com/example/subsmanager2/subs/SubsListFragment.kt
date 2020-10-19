package com.example.subsmanager2.subs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.subsmanager2.R
import com.example.subsmanager2.dao.SubsDao
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_subs_list.view.*

/**
 * A simple [Fragment] subclass.
 */
class SubsListFragment : Fragment() {

    // DAO
    val subsDao = SubsDao()

    //어댑터 생성
    val subsAdapter = SubsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_subs_list, container, false)

        /* 어댑터 초기화*/
        rootView.list_subs.adapter = subsAdapter
        rootView.list_subs.layoutManager = LinearLayoutManager(requireContext())

        if(subsAdapter != null) {
            rootView.txt_notice.visibility = View.GONE
        }

        /* 구독앱 추가 버튼 누르면 */
        rootView.fab_add_note.setOnClickListener {
            findNavController().navigate(R.id.action_subsListFragment_to_subsWriteFragment)
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

    override fun onStop() {
        super.onStop()
        //requireActivity().finish()
        //activity!!.finish()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
