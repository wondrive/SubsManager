//package com.example.subsmanager2.dao
//
//import androidx.lifecycle.LiveData
//import androidx.room.*
//import com.example.subsmanager2.entity.BoardEntity
//
//// 게시글 DAO (Data Access Object) : 데이터에 실제로 접근하는 명령 모음집
//@Dao
//interface BoardDao {
//
//    // key 중복시 strategy 설정
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertBoard(vararg board: BoardEntity)
//
//    // 상세
//    @Query("SELECT * FROM Board WHERE boardId = :boardId")
//    fun selectBoard(boardId: Long): BoardEntity?
//
//    // 게시글 전체
//    @Query("SELECT * FROM Board ORDER BY boardId DESC")
//    fun selectBoard(): LiveData<List<BoardEntity>>
//
//    @Query("SELECT * FROM Board WHERE boardId = :boardId")
//    fun selectLiveBoard(boardId: Long): LiveData<BoardEntity>
//
//
//    // 게시글 개수
//    @Query("SELECT COUNT(*) FROM Board")
//    fun countBoard(): Int
//
//    @Update
//    fun updateBoard(vararg board: BoardEntity)
//
//    @Delete
//    fun deleteBoardList(vararg board: BoardEntity)
//
//    @Query("DELETE FROM Board WHERE boardId = :boardId")
//    fun deleteBoard(boardId: Long)
//
//}
//
//
//
