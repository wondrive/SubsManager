package com.example.subsmanager2.subs

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.subsmanager2.R




class FragmentSubsUpdate : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val subsId = arguments?.getLong("SUBS_ID") ?: kotlin.run { throw Error("SUBS_ID가 없습니다.") }
        val updateYn = arguments?.getString("UPDATE_YN")?:kotlin.run { throw  Error("Update_YN이 없습니다.") }

        Log.d("subsid in update",subsId.toString())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_subs_update, container, false)
    }
}
