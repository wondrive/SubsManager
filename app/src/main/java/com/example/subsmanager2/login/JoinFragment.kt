package com.example.subsmanager2.login

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.subsmanager2.R
import com.example.subsmanager2.dao.UserDao
import com.example.subsmanager2.entity.UserEntity
import com.example.subsmanager2.util.hideKeyboard
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_join.*
import kotlinx.android.synthetic.main.fragment_join.view.*
import java.text.SimpleDateFormat
import java.util.*


class JoinFragment : Fragment() {

    //FirebaseAuth 객체의 공유 인스턴스를 가져오기
    val firebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val userDao by lazy { UserDao() }

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
            val id = view.edit_email.text.toString()
            val pw = view.edit_pw.text.toString()
            val pw_firm = view.edit_pw_confirm.text.toString()
            Log.d("debug","clicked join")
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

                    firebaseAuth.createUserWithEmailAndPassword(id, pw)
                        .addOnCompleteListener { task ->
                            /* 성공한 경우*/
                            task.addOnSuccessListener {

                                // Firebase 실시간DB : 데이터 입력
                                //var userId: String  = edit_id.text.toString().replace("@", "_").replace(".", "_") // 사용자 이메일에서 특수문자(@, .) 제거
                                val currentDateTime = Calendar.getInstance().time   // 현재시간 추출
                                var regDate: String = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).format(currentDateTime)   // 날짜만 형식대로 추출
                                var user = UserEntity(             // 엔터티가 가진 속성값 채워주기
                                    0,
                                    edit_email.text.toString(),
                                    edit_nickname.text.toString(),
                                    regDate,
                                    "구독계의 꿈나무"          // ㅋㅋ 임시 데이터
                                )
                                //firebaseRef.child("user").push().setValue(user)
                                userDao.insertUser(user)

                                //입력 필드 초기화
                                view.edit_email.text = null
                                view.edit_pw.text = null
                                view.edit_pw_confirm.text = null
                                view.register_loader.visibility = View.GONE

                                hideKeyboard()
                                findNavController().navigate(R.id.action_global_subsListFragment)
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
