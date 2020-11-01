package com.example.subsmanager2.entity

import com.google.firebase.database.Exclude

// 수동 등록 구독앱
data class RecommendEntity(    //
    var id: Long            = 0L,
    var planName:String     = "",
    var subsName:String     = "",
    var fee:String          = "",
    var boardRating:Float  = 0.0F,
    var imgUrl:String       = ""
) {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "planName" to planName,
            "subsName" to subsName,
            "fee" to fee,
            "boardRating" to boardRating,
            "imgUrl" to imgUrl
        )
    }
}
