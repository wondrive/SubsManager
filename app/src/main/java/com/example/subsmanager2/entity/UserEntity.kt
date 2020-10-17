package com.example.subsmanager2.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// 유저 객체라고 보면 됨

@Entity
data class UserEntity(
    @PrimaryKey
    val userId: String,
    val userNickname: String,
    val regDate: String,
    val userTitle: String
    //val role: Long             // 관리자 : 1, 회원 : 2
)