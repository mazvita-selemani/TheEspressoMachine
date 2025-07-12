package com.espresso.machine.model.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.espresso.machine.model.entity.Order
import com.espresso.machine.model.entity.OrderStatus
import com.espresso.machine.model.entity.OrderWithOrderItems
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {
    @Query("SELECT * FROM `order`")
    fun getAll(): Flow<List<Order>>

    @Query("SELECT * FROM `order` WHERE id IN (:orderIds)")
    fun getAllByIds(orderIds: IntArray): List<Order>

    @Query("SELECT * FROM `order` WHERE id = :orderId")
    fun getOrderById(orderId: Int): Order

    @Transaction
    @Query("SELECT * FROM `order` WHERE id = :orderId")
    fun getOrderWithOrderItems(orderId : Int): OrderWithOrderItems

    @Query("SELECT * FROM `order` WHERE user_id = :userId AND status = :status LIMIT 1")
    fun getOpenOrderForUser(userId: Int, status: OrderStatus = OrderStatus.IS_EMPTY): Order?

    @Query("UPDATE `order` SET status = :orderStatus WHERE id = :orderId")
    suspend fun updateOrderStatus(orderId: Int, orderStatus: OrderStatus)

    @Update
    suspend fun update(order: Order)

    @Insert
    suspend fun insertAll(vararg order: Order)

    @Insert
    suspend fun insert(order: Order)

    @Delete
    suspend fun delete(order: Order)
}