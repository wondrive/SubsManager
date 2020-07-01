package com.example.homecookhelper.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity @Parcelize
data class UserEntity(
    @PrimaryKey
    val userID: String,
    val userPW: String
) : Parcelable