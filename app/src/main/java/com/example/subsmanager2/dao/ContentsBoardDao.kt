package com.example.subsmanager2.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.subsmanager2.entity.ContentsBoardEntity

interface ContentsBoardDao {
    // 게시글 전체
    @Query("SELECT * FROM Board ORDER BY boardId DESC")
    fun selectBoard(): LiveData<List<ContentsBoardEntity>>

    @Query("SELECT * FROM Board WHERE boardId = :boardId")
    fun selectLiveBoard(boardId: Long): LiveData<ContentsBoardEntity>

    // 게시글 개수
    @Query("SELECT COUNT(*) FROM Board")
    fun countBoard(): Int

}