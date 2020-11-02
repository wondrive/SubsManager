package com.example.subsmanager2.subs

import android.os.Bundle
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.android.synthetic.main.fragment_subs_register.view.*

/**
 * A simple [Fragment] subclass.
 */
class SubsRegisterFragment : Fragment() {

    /* subs 객체 생성 및 초기화. */
    private var subs = SubsEntity(subsId = 0L, subsName = "", subsCustomName = "", userId = "", alarmYN = true, alarmDday = "", fee = "", feeDate = "")

    //subsDao 참조
    private val dao by lazy { SubsDao() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_subs_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //tag?.toLongOrNull()?.let { recipeId ->
        val updateYn = arguments?.getString("UPDATE_YN") ?: "N"
        if(updateYn.equals("Y")) {
            val subsId = arguments?.getLong("SUBS_ID") ?: kotlin.run { throw Error("SUBS_ID가 없습니다.") }
            viewLifecycleOwner.lifecycleScope.launch {
                /* 미리 변수를 선언 */
                var savedSubs: SubsEntity? = null
                /* 노트를 쿼리.*/
                withContext(Dispatchers.IO) {
                    savedSubs = dao.selectSubs(subsId)
                }
                /* 노트가 존재한다면 note를 변경*/

                savedSubs?.let {
                    subs = it //쿼리한 노트 객체를 note에 저장
                    view.edit_subs_name.setText(it.subsName)
                    view.edit_subs_custom_name.setText(it.subsCustomName)
                    view.edit_fee.setText(it.fee)
                    view.edit_fee_date.setText(it.feeDate)
                    val dday = when(it.alarmDday){
                        "1일 전" -> 0
                        "7일 전" -> 1
                        "14일 전" -> 2
                        else -> -1          // 오류
                    }
                    view.spinner_alarm_d_day.setSelection(dday)
                    view.switch_alarm.isChecked = subs.alarmYN
                    /*it.recipeImg?.let { recipeImg ->
                        view.img_profile.setImageURI(Uri.parse(recipeImg))
                    }*/
                }
            }

        }//end of let


        // 저장 버튼 누를 경우
        view.btn_save.setOnClickListener {
            //입력한 noteTitle, noteContent 가져와서 변수에 할당
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
                var userId: String? = ""
                FirebaseAuth.getInstance().currentUser?.let {
                    userId = it.email
                }

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
                        userId = userId.toString()
                    )
                    if(updateYn.equals("Y")) {   // 신규글인지 수정인지 구분하여 저장
                        dao.updateSubs(subs)
                    } else {
                        dao.insertSubs(subs)//DB에 저장
                    }
                }
                findNavController().popBackStack()
            }
        }//end of view.btn_save.setOnClickListener
    }//end of onViewCreated

}
