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
        val subsId = arguments?.getLong("SUBS_ID") ?: kotlin.run { throw Error("SUBS_ID가 없습니다.") }
        val userId = arguments?.getString("USER_ID") ?: kotlin.run { throw Error("USER_ID가 없습니다.") }

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
        */
        subsDao.selectLiveSubs(subsId)?.observe(viewLifecycleOwner, Observer {
            view.txt_subs_name.setText(it.subsName)
            Log.d(TAG, "Subs_Detail 출력 ::: " + it.subsName)
        })

        /*val subs:SubsEntity? = subsDao.selectSubs(subsId)
        if(subs != null) {
            txt_subs_name.setText(subs.subsName)
            Log.d(TAG, "Subs_Detail 성공 ::: " + subs.subsName)
        } else {
            Log.d(TAG, "Subs_Detail 실패 ::: ")
        }*/

        /* 수정버튼 */
        view.btn_detail_edit.setOnClickListener {
            //childFragmentManager:  Fragment에서 다른 Fragment를 만들 때(DetailFragment에서 DialFragment 만들 때)
            //RecipeWriteFragment().show(childFragmentManager, recipeId.toString())

            findNavController().navigate(R.id.action_subsDetailFragment_to_subsWriteFragment,
                Bundle().apply {
                    putLong("SUBS_ID", subsId!!)
                    putString("UPDATE_YN", "Y")
                })
        }

        /* Note 삭제버튼 */
        view.btn_detail_delete.setOnClickListener {
            /* 삭제*/
            lifecycleScope.launch(Dispatchers.IO){
                subsDao.deleteSubs(subsId)
            }
            Toast.makeText(requireContext(), "삭제되었습니다.", Toast.LENGTH_SHORT).show()
            //뒤로가기
            findNavController().popBackStack()
        }
    }//end of onViewCreated

}
