package com.example.homecookhelper.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.homecookhelper.R
import com.example.subway.util.hideKeyboard
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_join.view.*

/**
 * A simple [Fragment] subclass.
 */
class JoinFragment : Fragment() {

    //FirebaseAuth 객체의 공유 인스턴스를 가져오기
    val firebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_join, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //회원 가입 버튼을 클릭한 경우
        view.btn_join.setOnClickListener {
            //id, pw, pw_firm 값 가져오기
            val id = view.edit_id.text.toString()
            val pw = view.edit_pw.text.toString()
            val pw_firm = view.edit_pw_confirm.text.toString()

            //id, pw, pw_firm 입력 여부 확인
            when {
                id.isEmpty() -> Toast.makeText(requireContext(), "아이디를 입력하세요.", Toast.LENGTH_LONG)
                    .show()
                pw_firm.isEmpty() || pw.isEmpty() -> Toast.makeText(
                    requireContext(),
                    "패스워드를 입력하세요.",
                    Toast.LENGTH_LONG
                ).show()
                pw_firm != pw -> Toast.makeText(
                    requireContext(),
                    "비밀번호가 일치하지 않습니다.",
                    Toast.LENGTH_LONG
                ).show()
                else -> {
                    //id, pw, pw_firm 입력값이 정상이면
                    view.register_loader.visibility = View.VISIBLE

                    /* 신규 사용자의 이메일 주소와 비밀번호를 createUserWithEmailAndPassword에 전달하여
                       신규 계정을 생성하고, searchFragment로 이동
                       - 실패한 경우 에러 메시지 출력
                     */
                    firebaseAuth.createUserWithEmailAndPassword(id, pw)
                        .addOnCompleteListener { task ->
                            /* 성공한 경우*/
                            task.addOnSuccessListener {
                                //입력 필드 초기화
                                view.edit_id.text = null
                                view.edit_pw.text = null
                                view.edit_pw_confirm.text = null
                                view.register_loader.visibility = View.GONE

                                hideKeyboard()
                                findNavController().navigate(R.id.action_global_agricListFragment)
                            }
                            /* 실패한 경우*/
                            task.addOnFailureListener {
                                view.register_loader.visibility = View.GONE
                                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }//end of firebaseAuth.createUserWithEmailAndPassword.addOnCompleteListener
                }//end of when-else
            }//end of when
        }//end of view.btn_register.setOnClickListener
    }//end of onViewCreated

}
