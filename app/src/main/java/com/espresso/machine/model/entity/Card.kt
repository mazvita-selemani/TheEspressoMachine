package com.espresso.machine.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class Card(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "user_id") val userId: Int?,
    @ColumnInfo(name = "card_holder_name") val cardHolderName: String?,
    @ColumnInfo(name = "card_number") val cardNumber: String?,
    @ColumnInfo(name = "expiry_date") val expiryDate: String?,
    @ColumnInfo(name = "cvc") val cvc: String?
)
