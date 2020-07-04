package com.example.homecookhelper.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "Recipe")
data class RecipeEntity(    // ID, 제목, 내용, 글쓴이, 이미지, (게시일, 수정일) => 필요시 추후 추가
    @PrimaryKey(autoGenerate = true)
    var recipeId: Long ?= null,
    var userId:String,      //
    var recipeTitle:String,     // 제목
    var recipeContent:String,   // 내용
    var recipeImg:String?       // 이미지
)

/*@Entity(tableName = "SaveRecipe")
data class SaveRecipe(
    @PrimaryKey(autoGenerate = true)
    //var agricId: Long? = null,
    var userId: Long?=null
)*/
