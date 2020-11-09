package com.example.subsmanager2.subs

import android.app.AppOpsManager
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Process
import android.provider.Settings
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
import kotlinx.android.synthetic.main.fragment_subs_detail.view.*
import kotlinx.android.synthetic.main.fragment_subs_detail.view.txt_subs_name
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*


class SubsDetailFragment : Fragment() {

    val TAG = "SubsDetailFragment"

    //dao 참조
    private val subsDao by lazy { SubsDao() }

    private var subsId:Long =0
    private var userId:String=""
    private var updateYn:String=""
    private var appName:String=""

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
        subsId = arguments?.getLong("SUBS_ID") ?: kotlin.run { throw Error("SUBS_ID가 없습니다.") }
        userId = arguments?.getString("USER_ID") ?: kotlin.run { throw Error("USER_ID가 없습니다.") }
        updateYn = arguments?.getString("UPDATE_YN")?:kotlin.run { throw  Error("Update_YN이 없습니다.") }

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

        //데이터 파싱
        val firestore by lazy { FirebaseFirestore.getInstance() }
        var subs = SubsEntity()
        firestore.collection("subs").whereEqualTo("subsId", subsId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                for(document in documentSnapshot!!) {
                    subs = document.toObject(SubsEntity::class.java)
                    view.txt_subs_name.text=subs.subsName
                    view.txt_charge_date.text=subs.feeDate
                    view.txt_alarm_d_day.text=subs.alarmDday
                    /**
                     * 사용시간 가져오기
                     */
                    //appName = "youtube"
                    appName=subs.subsName
                    /**
                     * 1. checkForPermission으로 USAGE_ACCESS 권한을 설정했는지 확인함
                     */
                    if (!checkForPermission()) {
                        Log.i(TAG, "The user may not allow the access to apps usage. ")
                        startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
                    } else {
                        /**
                         * 2. showAppUsageStats로 해당하는 appName의 사용시간을 조회한다
                         */
                        val usageStats = getAppUsageStats()
                        view.txt_usage.text = "이번 달에 '"+subs.fee+"'원을 지출하고 "+showAppUsageStats(usageStats,appName).toString() +"분 사용했습니다."
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Subs 상세 읽기 실패: ", exception)
         }

        // 데이터 수정
        view.btn_detail_edit.setOnClickListener {
            //childFragmentManager:  Fragment에서 다른 Fragment를 만들 때(DetailFragment에서 DialFragment 만들 때)
            //RecipeWriteFragment().show(childFragmentManager, recipeId.toString())
            findNavController().navigate(R.id.action_subsDetailFragment_to_fragmentSubsUpdate,
                Bundle().apply {
                    putLong("SUBS_ID", subsId!!)
                    putString("UPDATE_YN",updateYn!!)
                    putString("USER_ID",userId!!)
                })
        }

        // 데이터 삭제
        view.btn_detail_delete.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO){
                subsDao.deleteSubs(subsId)
            }
            Toast.makeText(requireContext(), "삭제되었습니다.", Toast.LENGTH_SHORT).show()
            //뒤로가기
            findNavController().popBackStack()
        }

    }//end of onViewCreated

    /**
     * package name 추출한다
     */
    fun getPackageName(subname: String): String{
        var rs=""
        when(subname){
            "watcha" -> rs="wplay"
            "왓챠" -> rs="wplay"
            "youtube" -> rs = "youtube"
            "밀리의 서재" -> rs = "milli"
            "넷플릭스" -> rs = "netflix"
            "netflix" -> rs = "netflix"
            "리디북스" -> rs = "ridi"
            "지니" -> rs = "genie"
            "멜론" -> rs = "melon"
        }
        return  rs
    }


    /**
     *  USAGE_STATS 허가 받았는지 확인하는 매소드
     */
    fun checkForPermission(): Boolean {
        val appOps = activity?.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val packageName = context?.packageName
        val mode = appOps.checkOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            Process.myUid(),
            packageName
        )
        return mode == AppOpsManager.MODE_ALLOWED
    }

    /**
     * 한달간 사용 시간량을 조회함
     */
    private fun getAppUsageStats(): MutableList<UsageStats> {
        val cal = Calendar.getInstance()
        // 한달로 설정
        cal.add(Calendar.MONTH, -1)

        val usageStatsManager = activity?.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val queryUsageStats = usageStatsManager
            .queryUsageStats(
                UsageStatsManager.INTERVAL_MONTHLY, cal.timeInMillis,
                System.currentTimeMillis()
            )
        return queryUsageStats
    }


    private fun showAppUsageStats(usageStats: MutableList<UsageStats>, appName: String): Double {
        var usageTime: Double = 0.0

        usageStats.sortWith(Comparator { right, left ->
            compareValues(left.lastTimeUsed, right.lastTimeUsed)
        })

        /**
         * 3. 시스템에서 사용하는 package name을 받아온다
         */
        val appname = getPackageName(appName)

        usageStats.forEach { it ->
            if(it.packageName.contains(appname) ) {
                /**
                 * it.totalTimeInForeground 은 ms값을 받아오므로 분 단위로 바꿔준다
                 */
                usageTime = (it.totalTimeInForeground/60000).toDouble()
                Log.e(
                    TAG,
                    "packageName: ${it.packageName}, lastTimeUsed: ${Date(it.lastTimeUsed)}, " +
                            "totalTimeInForeground: ${it.totalTimeInForeground}"
                )
            }
        }
        Log.e("usageTime: ",usageTime.toString())
        /**
         * 분단위로 usageTime 값을 반환한다.
         */
        return usageTime
    }

}
