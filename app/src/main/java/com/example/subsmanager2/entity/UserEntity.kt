package com.example.subsmanager2.entity

import com.google.firebase.database.IgnoreExtraProperties

// 유저 클래스
@IgnoreExtraProperties
data class UserEntity(
    val userId: String,
    val userNickname: String,
    val regDate: String,
    val userTitle: String
    //val role: Long             // 관리자 : 1, 회원 : 2
)