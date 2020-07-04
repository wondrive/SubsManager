package com.example.homecookhelper.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(
    tableName = "Recipe",
    foreignKeys = arrayOf(
        ForeignKey(
            onDelete = ForeignKey.CASCADE,
            entity = UserEntity::class,
            parentColumns = arrayOf("userId"),
            childColumns = arrayOf("userId")
        )
    )
)
data class RecipeEntity(    // ID, 제목, 내용, 글쓴이, 이미지, (게시일, 수정일) => 필요시 추후 추가
    @PrimaryKey(autoGenerate = true)
    var recipeId: Int ?= null,
    var userId:String,      // ForeignKey

    var recipeTitle:String,
    var recipeDetail:String,
    var recipeImg:String?
)

/*@Entity(tableName = "SaveRecipe")
data class SaveRecipe(
    @PrimaryKey(autoGenerate = true)
    //var agricId: Long? = null,
    var userId: Long?=null
)*/
