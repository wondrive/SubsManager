package com.example.subsmanager2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_mypage.view.*


class MypageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mypage, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        FirebaseAuth.getInstance().currentUser?.let {
            view.txt_userinfo.text = it.email
        }

        //로그아웃 버튼을 클릭하면
        view.btn_logout.setOnClickListener {
            /* 사용자를 로그아웃하고 loginFragment로 이동
               - 사용자를 로그아웃시키려면 signOut() 호출  */
            FirebaseAuth.getInstance().signOut()
            findNavController().navigate(R.id.action_global_loginFragment)
        }
    }
}
