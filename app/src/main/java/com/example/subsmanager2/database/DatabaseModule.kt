package com.example.subsmanager2.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.subsmanager2.dao.AgricDao
import com.example.subsmanager2.dao.BoardDao
import com.example.subsmanager2.entity.AgricEntity
import com.example.subsmanager2.entity.BoardEntity

// 사용할 DB 선언 되어있는 파일.

@Database(entities = arrayOf(AgricEntity::class, BoardEntity::class), version = 3)
abstract class DatabaseModule : RoomDatabase() {

    /* Query 문에 사용하는 Dao가져오기. */
    abstract fun agricDao(): AgricDao
    abstract fun boardDao(): BoardDao

    companion object {
        // database 변수 선언
        private var database: DatabaseModule? = null

        //database 이름 상수 선언
        private const val ROOM_DB = "subsmanager2.db"

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