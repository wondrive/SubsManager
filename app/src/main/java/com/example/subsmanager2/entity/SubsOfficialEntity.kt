package com.example.subsmanager2.entity

import com.google.firebase.database.Exclude

// 구독앱 Entity (실존하는 공식 구독앱)
data class SubsOfficialEntity(    //
    var categoryId:Long     = 0L,
    var id: Long            = 0L,
    var imgUrl:String       = "",
    var name:String         = "",
    var systemName:String   = ""
) {
    //@Exclude
    public fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "name" to name,
            "systemName" to systemName,
            "imgUrl" to imgUrl,
            "categoryId" to categoryId
        )
    }
}
