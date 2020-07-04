package com.example.homecookhelper.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.homecookhelper.entity.AgricEntity
import com.example.homecookhelper.entity.RecipeEntity

// 레시피 DAO
@Dao
interface RecipeDao {

    // key 중복시 strategy 설정
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecipes(vararg recipes: RecipeEntity)

    // 레시피
    @Query("SELECT * FROM Recipe WHERE recipeId = :recipeId")
    fun selectRecipe(recipeId: Long): RecipeEntity?

    // 레시피 전체
    @Query("SELECT * FROM Recipe")
    fun selectRecipe(): LiveData<List<RecipeEntity>>

    @Query("SELECT * FROM Recipe WHERE recipeId = :recipeId")
    fun selectLiveRecipe(recipeId: Long): LiveData<RecipeEntity>

    @Update
    fun updateRecipe(vararg recipe: RecipeEntity)

    @Delete
    fun deleteRecipes(vararg recipe: RecipeEntity)

    @Query("DELETE FROM Recipe WHERE recipeId = :recipeId")
    fun deleteRecipe(recipeId: Long)
}

