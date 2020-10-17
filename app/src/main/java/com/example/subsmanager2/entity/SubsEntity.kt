package com.example.subsmanager2.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

// 수동 등록 구독앱
@Entity
data class SubsEntity(    // 필요시 추후 변경
    //@PrimaryKey(autoGenerate = true)
    //var subsId: Long ?= null,   // 구독앱 고유 ID
    var subsNickname:String,   // 구독앱 별칭======> 이걸 고유키로 써야할듯ㅠㅠ
    var userId:String,          // 글쓴이 ID
    var subsName:String,       // 구독앱 이름
    var subsImg:String?,        // 이미지
    var fee:String,            // 요금
    //var feeName:String,        // 요금제 이름
    var alarmYN:String,        // 알람 여부
    var alarmDday:String,      // 알람 디데이
    var alarmMethod:String      // 알람 방식 (팝업 or
)