package com.espresso.machine.model.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.espresso.machine.model.entity.MilkType
import com.espresso.machine.model.entity.OrderItem
import com.espresso.machine.model.entity.Size
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderItemDao {
    @Query("SELECT * FROM orderitem")
    fun getAll(): Flow<List<OrderItem>>

    @Query("SELECT * FROM orderitem WHERE id IN (:orderItemIds)")
    fun getAllByIds(orderItemIds: IntArray): List<OrderItem>

    @Query("SELECT * FROM orderitem WHERE id = :orderItemId")
    fun getOrderItemById(orderItemId: Int): OrderItem

    @Update
    suspend fun update(orderItem: OrderItem)

    @Query("INSERT INTO orderitem (order_id, food_item_id, is_updated, size, milk_type, item_price_total, quantity) VALUES (:orderId, :foodItemId, :isUpdated, :size, :milkType, :itemPriceTotal, :quantity)")
    suspend fun insertCustom(
        orderId: Int?,
        foodItemId: Int?,
        isUpdated: Boolean?,
        size: Size?,
        milkType: MilkType?,
        itemPriceTotal: Double?,
        quantity: Int?
    )

    @Insert
    suspend fun insert(orderItem: OrderItem)

    @Delete
    suspend fun delete(orderItem: OrderItem)
}