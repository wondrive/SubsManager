package com.example.subsmanager2.entity

// 유저 클래스
data class UserEntity(
    var userNo: Long            = 0L,
    var userEmail: String       = "",
    var userNickname: String    = "",
    var regDate: String         = "",
    var title: String           = ""
    //val role: Long             // 관리자 : 1, 회원 : 2
)