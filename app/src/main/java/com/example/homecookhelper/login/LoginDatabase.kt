package com.example.homecookhelper.login

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.homecookhelper.entity.UserEntity

// DB에 포함된 엔티티 목록 가져옴, version: DB구조 바뀌면 같이 바뀜
@Database(entities = arrayOf(UserEntity::class), version = 1)
abstract class LoginDatabase : RoomDatabase() {

    abstract fun loginDao(): LoginDao

    companion object {
        private var db: LoginDatabase? = null     // DB 변수 선언
        private const val ROOM_DB = "login.db"           // DB 이름 상수 선언


        fun getDatabase(context: Context): LoginDatabase {
            if(db == null) {
                db = Room.databaseBuilder(
                    context.applicationContext, LoginDatabase::class.java, ROOM_DB
                ).build()
            }
            return db!!;    // 안전한 강제 캐스팅
        }
    }
}