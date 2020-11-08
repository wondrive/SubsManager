package com.example.subsmanager2

import android.app.Activity
import android.app.AppOpsManager
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Process
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.pedro.library.AutoPermissions
import com.pedro.library.AutoPermissions.Companion.parsePermissions
import com.pedro.library.AutoPermissionsListener
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


/**AutoPermissionsListener*/
class MainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val controller = findNavController(R.id.navigation_host)

        NavigationUI.setupWithNavController(
            bottom_navigation, controller
        )

        controller.addOnDestinationChangedListener { _, destination, _ ->
            /* 최종 destination에서 네비바 없애기*/
            if (arrayListOf(
                    R.id.subsListFragment,
                    R.id.recommendListFragment,
                    R.id.boardListFragment,
                    R.id.mypageFragment
                ).contains(destination.id)
            ) {
                bottom_navigation.visibility = View.VISIBLE
            } else {
                bottom_navigation.visibility = View.GONE
            }
        }

        /**
         * 앱 접근 허가 요청 -> 앱 실행되자마자 승인을 받으러감
         */
        if (!checkForPermission()) {
            Log.i("_", "The user may not allow the access to apps usage. ")
            Toast.makeText(
                this,
                "Failed to retrieve app usage statistics. " +
                        "You may need to enable access for this app through " +
                        "Settings > Security > Apps with usage access",
                Toast.LENGTH_LONG
            ).show()
              startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
        }else{
            Log.e("permission","get success")
        }
    }
    /** 팝업창으로 허가 요청 받으려했는데, 구현이 안됐음
    override fun onDenied(requestCode: Int, permissions: Array<String>) {
        TODO("Not yet implemented")
    }

    override fun onGranted(requestCode: Int, permissions: Array<String>) {
        TODO("Not yet implemented")
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    */

    private fun checkForPermission(): Boolean {
        val appOps = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.checkOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            Process.myUid(),
            packageName
        )
        return mode == AppOpsManager.MODE_ALLOWED
    }















    private fun getAppUsageStats(): MutableList<UsageStats> {
        val cal = Calendar.getInstance()
        cal.add(Calendar.YEAR, -1)

        val usageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val queryUsageStats = usageStatsManager
            .queryUsageStats(
                UsageStatsManager.INTERVAL_MONTHLY, cal.timeInMillis,
                System.currentTimeMillis()
            )
        return queryUsageStats
    }
    private fun showAppUsageStats(usageStats: MutableList<UsageStats>, appName: String) {
        usageStats.sortWith(Comparator { right, left ->
            compareValues(left.lastTimeUsed, right.lastTimeUsed)
        })
        usageStats.forEach { it ->
            if(it.packageName.contains(appName)) {
                Log.e(
                    "",
                    "packageName: ${it.packageName}, lastTimeUsed: ${Date(it.lastTimeUsed)}, " +
                            "totalTimeInForeground: ${it.totalTimeInForeground}"
                )
            }
        }
    }

    // 뒤로가기 버튼 눌렀을 때, 안꺼지고 뒤로가기
    override fun onSupportNavigateUp() = findNavController(R.id.navigation_host).navigateUp()


}
