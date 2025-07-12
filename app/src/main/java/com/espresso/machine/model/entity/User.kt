package com.espresso.machine.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
@PrimaryKey (autoGenerate = true) val id: Int= 0,
    @ColumnInfo(name = "first_name") val firstName: String?,
    @ColumnInfo(name = "last_name") val lastName: String?,
    @ColumnInfo(name = "email_address") val emailAddress: String?,
    @ColumnInfo(name = "password") val password: String?,
    @ColumnInfo(name = "available_card_id") val availableCardId: Int?
)