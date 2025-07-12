package com.espresso.machine.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

enum class Category {
    HOT_DRINK,
    COLD_DRINK,
    BAKED_GOODS
}

// adding default values for schema path to be exported properly
@Entity
data class Food(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "category") val category: Category?,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "price") val price: Double?,
    @ColumnInfo(name = "image_resource_id") val resId: Int?
)
