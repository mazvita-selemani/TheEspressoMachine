package com.espresso.machine.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

enum class  MilkType{
    ALMOND,
    COCONUT,
    OAT
}

enum class Size{
    SMALL,
    MEDIUM,
    LARGE
}

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Food::class,
            parentColumns = ["id"],
            childColumns = ["food_item_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class OrderItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "order_id") val orderId : Int?,
    @ColumnInfo(name = "food_item_id") val foodItemId: Int?,
    @ColumnInfo(name = "food_name") val foodName: String?,
    @ColumnInfo(name = "food_image_resource") val foodResId: Int?,
    @ColumnInfo(name = "is_updated") val isUpdated: Boolean?,
    @ColumnInfo(name = "size") val size: Size?,
    @ColumnInfo(name = "milk_type") val milkType: MilkType?,
    @ColumnInfo(name = "item_price_total") val itemPriceTotal: Double?, // total calculated from quantity
    @ColumnInfo(name = "quantity") val quantity: Int?,
)
