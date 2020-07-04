package com.example.homecookhelper.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.homecookhelper.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A simple [Fragment] subclass.
 */
class SplashFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {


        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch(Dispatchers.IO) {
            Thread.sleep(2000)

            withContext(Dispatchers.Main) {
                /* 현재 로그인한 사용자 가져오기
                   - 로그인 user가 있으면  global 액션으로 바로 searchFragment로 이동하고
                   - 로그인 user가 없으면(null) global 액션으로 바로 loginFragment로 이동
                 */
                FirebaseAuth.getInstance().currentUser?.let {
                    findNavController().navigate(R.id.action_global_agricListFragment)
                } ?: kotlin.run {
                    findNavController().navigate(R.id.action_global_loginFragment)
                }
            }//end of withContext
        }//end of ifecycleScope.launch
    }//end of onViewCreated

}
