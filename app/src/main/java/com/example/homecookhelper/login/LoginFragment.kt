package com.example.homecookhelper.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.homecookhelper.R

/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        /*
        btn_login.setOnClickListener {
            if(loginValidate()) {
                lifecycleScope.launch(Dispatchers.Main) {
                    var userInfo: UserEntity? = null

                    //withContext(Dispatchers.IO)
                }
            }

        }*/


        return inflater.inflate(R.layout.fragment_login, container, false)
    }


    /*
    // ID, PW 입력 여부 검증
    fun loginValidate(): Boolean{
        if(edit_id.text.toString().isBlank() || edit_pw.text.toString().isBlank()) {
            Toast.makeText(this, "아이디나 패스워드를 입력해주세요.", Toast.LENGTH_SHORT).show()
            return false
        } else {
            return true
        }
    }

    // 입력필드 초기화
    fun clearTextField() {
        edit_id.setText("")
        edit_pw.setText("")
    }

    // final static 상수 선언
    companion object {
        const val PARCELABLE_USERINFO = "PARCELABLE_USERINFO"
    }*/
}
