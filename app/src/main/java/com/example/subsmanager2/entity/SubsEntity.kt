package com.example.subsmanager2.entity

import com.google.firebase.database.Exclude

// 수동 등록 구독앱
data class SubsEntity(    //
    var subsId: Long            = 0L,           // 구독앱 고유 ID
    var subsName:String         = "",       // 구독앱 이름
    var subsCustomName:String   = "",       // 구독앱 별칭
    var userId:String?          = "",            // 글쓴이 ID
    //var subsImg:String?,          // 이미지 경로
    var fee:String              = "",               // 요금
    var feeDate: String         = "",          // 결제일
    //var feeName:String,        // 요금제 이름
    var alarmYN:Boolean         = false,          // 알람 여부
    var alarmDday:String        = ""       // 알람 디데이
    //var alarmMethod:String      // 알람 방식 (팝업 or
) {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "subsId" to subsId,
            "subsName" to subsName,
            "subsCustomName" to subsCustomName,
            "userId" to userId,
            //"subsImg" to subsImg,
            "fee" to fee,
            "feeDate" to feeDate,
            "alarmYN" to alarmYN,
            "alarmDday" to alarmDday
        )
    }
}
