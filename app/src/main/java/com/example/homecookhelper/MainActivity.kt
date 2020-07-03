package com.example.homecookhelper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        NavigationUI.setupWithNavController(
           bottom_navigation, findNavController(R.id.navigation_host)
        )


        // Top Level Destination 설정. (더이상 뒤로가기 안되는 부분)
        NavigationUI.setupActionBarWithNavController(
            this,
            findNavController(R.id.navigation_host),
            AppBarConfiguration(
                setOf(
                    R.id.agricListFragment,
                    R.id.agricSearchFragment,
                    R.id.recipeListFragment,
                    R.id.recipeBookmarkFragment,
                    R.id.infoFragment
                )
            )
        )
    }

    // 뒤로가기 버튼 눌렀을 때, 안꺼지고 뒤로가기
    override fun onSupportNavigateUp() = findNavController(R.id.navigation_host).navigateUp()
}
