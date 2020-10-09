package com.example.subsmanager2.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

// 유저 객체라고 보면 됨

@Entity @Parcelize
data class UserEntity(
    @PrimaryKey
    val userId: String,
    val userPw: String
) : Parcelable