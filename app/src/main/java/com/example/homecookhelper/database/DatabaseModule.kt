package com.example.homecookhelper.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.homecookhelper.dao.AgricDao

abstract class DatabaseModule : RoomDatabase() {

    /* Query 문에 사용하는 Dao가져오기. */
    abstract fun agricDao(): AgricDao

    companion object {
        // database 변수 선언
        private var database: DatabaseModule? = null

        //database 이름 상수 선언
        private const val ROOM_DB = "homecookhelper.db"

        /* 정의한 Database 객체를 가져오는 함수 선언 */
        fun getDatabase(context: Context): DatabaseModule {
            if (database == null) {
                database = Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseModule::class.java, ROOM_DB
                ).allowMainThreadQueries().fallbackToDestructiveMigration().build()
            }
            /* 안전한 강제 캐스팅 */
            return database!!
        }
    }
}