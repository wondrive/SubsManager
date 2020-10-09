package com.example.subsmanager2.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.subsmanager2.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_login.view.*

//로그인 Fragment - Android에서 비밀번호 기반 계정으로 Firebase에 인증
class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* 회원가입 user인 경우 */
        view.btn_join.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_joinFragment)
        }

        /* 로그인하는 user인 경우 */
        view.btn_login.setOnClickListener {
            //입력한 id와 password 가져오기
            val id = view.edit_id.text.toString()
            val password = view.edit_pw.text.toString()

            //id와 password 입력여부 확인
            if (id.isBlank() || password.isBlank()) {
                Toast.makeText(requireContext(), "아이디/패스워드 입력해주세요", Toast.LENGTH_LONG).show()
            } else {
                //progress_loader VISIBLE
                view.progress_loader.visibility = View.VISIBLE

                /* 이메일 주소와 비밀번호로 사용자 로그인 처리하기
                   - FirebaseAuth.getInstance(): Initialize Firebase Auth
                   - id(이메일 주소)와 password를 signInWithEmailAndPassword()에 전달하여
                     유효성을 검사한 후, 등록된 user인경우 searchFragment로 이동
                 */
                FirebaseAuth.getInstance().signInWithEmailAndPassword(id, password)
                    .addOnCompleteListener { task ->
                        /* 등록된 user인 경우*/
                        task.addOnSuccessListener {
                            //입력 필드 초기화
                            view.edit_id.text = null
                            view.edit_pw.text = null
                            view.progress_loader.visibility = View.GONE

                            //hideKeyboard()
                            findNavController().navigate(R.id.action_global_agricListFragment)
                        }
                        /* 등록한 user가 아닌 경우*/
                        task.addOnFailureListener {
                            view.progress_loader.visibility = View.GONE
                            /* Exception을 받아 에러메세지 출력 */
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }//end of FirebaseAuth.getInstance().signInWithEmailAndPassword.addOnCompleteListener
            }//end of view.btn_login.setOnClickListene-else
        }//end of view.btn_login.setOnClickListene
    }//end of onViewCreated
}//end of LoginFragment
