package com.espresso.machine.model.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation


//TODO
enum class OrderStatus {
    IS_EMPTY,
    IS_IN_CART,
    AWAITING_CONFIRMATION,
    PREPARING,
    ON_ITS_WAY,
    DONE
}

@Entity(
    tableName = "order",
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
data class Order(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "user_id") val userId: Int?,
    @ColumnInfo(name = "is_empty") val isEmpty: Boolean?,
    @ColumnInfo(name = "total_price") val totalPrice: Double?,
    @ColumnInfo(name = "status") val status: OrderStatus?
)

// establishing a one to many relationship between order and order items
data class OrderWithOrderItems(
    @Embedded val order: Order,
    @Relation(
        parentColumn = "id",
        entityColumn = "order_id"
    )
    val orderItems: MutableList<OrderItem>
)