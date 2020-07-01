package com.example.homecookhelper.login

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.homecookhelper.entity.UserEntity

@Dao
interface LoginDao {

    // Insert:: 신규 가입
    @Insert(onConflict = OnConflictStrategy.REPLACE) // key 중복시 Strategy설정
    fun insertUser(userEntity: UserEntity)
    
    // SELECT:: 회원 검색, 로그인
    @Query("SELECT * FROM UserEntity WHERE userID = :userID and userPW = :userPW")
    fun selectUser(userID: String, userPW: String): UserEntity?


}