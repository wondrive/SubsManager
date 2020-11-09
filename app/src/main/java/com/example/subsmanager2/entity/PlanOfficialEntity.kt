package com.example.subsmanager2.entity

import com.google.firebase.database.Exclude

// 수동 등록 구독앱
data class PlanOfficialEntity(    //
    var id: Long            = 0L,
    var name:String         = "",
    var fee:String          = "",
    var boardRating:Float  = 0.0F,
    var subsName:String     = "",
    var categoryName: String = "",
    var imgUrl:String =""
) {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "name" to name,
            "fee" to fee,
            "boardRating" to boardRating,
            "imgUrl" to imgUrl,
            "subsName" to subsName
        )
    }
}
