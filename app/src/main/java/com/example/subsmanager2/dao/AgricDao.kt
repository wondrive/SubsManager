package com.example.subsmanager2.dao

import androidx.room.Dao

// 농산물 DAO
@Dao
interface AgricDao {
/*    // 이달의 List
    @Query("SELECT * FROM Agric WHERE month <= :month AND (month+monthPlus) >= :month")
    fun selectAgrics(month: String): LiveData<List<AgricEntity>>

    // Search List
    @Query("SELECT * FROM Agric WHERE agricName = :agricName")
    fun selectSearchAgrics(agricName: String): LiveData<List<AgricEntity>>

    // Agric Detail
    @Query("SELECT * FROM Agric WHERE agricId = :agricId")
    fun selectAgric(agricId: String): LiveData<AgricEntity>*/
}

