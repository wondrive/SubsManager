package com.example.subsmanager2.subs

import android.content.ContentValues.TAG
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.subsmanager2.R
import com.example.subsmanager2.dao.SubsDao
import com.example.subsmanager2.entity.SubsEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_subs_update.view.*
import kotlinx.android.synthetic.main.fragment_subs_update.view.edit_fee
import kotlinx.android.synthetic.main.fragment_subs_update.view.edit_fee_date
import kotlinx.android.synthetic.main.fragment_subs_update.view.edit_subs_custom_name
import kotlinx.android.synthetic.main.fragment_subs_update.view.edit_subs_name
import kotlinx.android.synthetic.main.fragment_subs_update.view.spinner_alarm_d_day
import kotlinx.android.synthetic.main.fragment_subs_update.view.switch_alarm
import kotlinx.android.synthetic.main.fragment_subs_register.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FragmentSubsUpdate : Fragment() {

    var dao = SubsDao()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val subsId = arguments?.getLong("SUBS_ID") ?: kotlin.run { throw Error("SUBS_ID가 없습니다.") }
        val userId = arguments?.getString("USER_ID")?: kotlin.run { throw Error("USER_ID 없습니다.") }
        val updateYn = arguments?.getString("UPDATE_YN")?:kotlin.run { throw  Error("UPDATE_YN이 없습니다.") }

        //데이터 파싱
        val firestore by lazy { FirebaseFirestore.getInstance() }
        var subs = SubsEntity()
        firestore.collection("subs").whereEqualTo("subsId", subsId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                for(document in documentSnapshot!!) {
                    Log.d(TAG, "Subs_Detail  ::: " + document.data)
                    subs = document.toObject(SubsEntity::class.java)
                    view.edit_subs_name.text=subs.subsName.toEditable()
                    view.edit_subs_custom_name.text=subs.subsCustomName.toEditable()
                    view.edit_fee.text=subs.fee.toEditable()
                    view.edit_fee_date.text=subs.feeDate.toEditable()
                    subs.alarmDday
                    if(subs.alarmYN){
                        view.switch_alarm.isChecked = true
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Subs 상세 읽기 실패: ", exception)
            }

        // 수정하기 버튼 클릭
        view.btn_update.setOnClickListener {
            //입력한 noteTitle, noteContent 가져와서 변수에 할당
            subs.subsId=subsId
            subs.userId=userId
            subs.subsName = view.edit_subs_name.text.toString()
            subs.subsCustomName = view.edit_subs_custom_name.text.toString()
            subs.fee = view.edit_fee.text.toString()
            subs.feeDate = view.edit_fee_date.text.toString()
            subs.alarmDday = view.spinner_alarm_d_day.selectedItem.toString()
            subs.alarmYN = view.switch_alarm.isChecked

            /* 제목과 내용이 다 있는지를 검증 */
            if (subs.subsName.isBlank() && subs.subsCustomName.isBlank() && subs.fee.isBlank() && subs.feeDate.isBlank() && subs.alarmDday.isBlank()) {
                Toast.makeText(requireContext(), "정확히 입력해주세요", Toast.LENGTH_LONG).show()
            } else {
                /* 자동 스코프에 맞추어 코루틴을 실행*/
                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                    val subs = SubsEntity(
                        subsId = subs.subsId,
                        subsName = subs.subsName,
                        subsCustomName = subs.subsCustomName,
                        fee = subs.fee,
                        feeDate = subs.feeDate,
                        alarmYN = subs.alarmYN,
                        alarmDday = subs.alarmDday,
                        userId = subs.userId
                    )
                    dao.updateSubs(subs)
                }
                findNavController().popBackStack()
            }
        }//end of view.btn_save.setOnClickListener
    }

    //수정 가능한 텍스트로 캐스트 해주는 함수
    fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_subs_update, container, false)
    }
}


