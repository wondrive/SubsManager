package com.example.subsmanager2.entity

import com.google.firebase.database.Exclude

// 카테고리 Entity
data class CategoryEntity(    //
    var id: Long = 0L,
    var name: String = ""
) {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "name" to name
        )
    }
}
