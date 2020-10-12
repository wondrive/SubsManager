//package com.example.subsmanager2.entity
//
//import androidx.room.Entity
//import androidx.room.PrimaryKey
//
//@Entity(tableName = "Board")
//data class BoardEntity_origin(    // ID, 제목, 내용, 글쓴이, 이미지, (게시일, 수정일) => 필요시 추후 추가
//    @PrimaryKey(autoGenerate = true)
//    var boardId: Long ?= null, // 게시글 고유 ID
//    var userId:String,         // 글쓴이
//    var boardTitle:String,     // 제목
//    var boardContent:String,   // 내용
//    var boardImg:String?       // 이미지
//)