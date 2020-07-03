package com.example.homecookhelper.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.homecookhelper.entity.AgricEntity

// 농산물 DAO
@Dao
interface AgricDao {
    // 이달의 List
    @Query("SELECT * FROM Agric WHERE month <= :month AND (month+monthPlus) >= :month")
    fun selectAgricss(month: String): List<AgricEntity>?

    // Search List
    @Query("SELECT * FROM Agric WHERE agricName = :agricName")
    fun selectAgrics(agricName: String): List<AgricEntity>?

    // Agric Detail
    @Query("SELECT * FROM Agric WHERE agricId = :agricId")
    fun selectAgric(agricId: String): AgricEntity
}


