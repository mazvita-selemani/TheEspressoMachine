package com.espresso.machine.model.repository

import androidx.annotation.WorkerThread
import com.espresso.machine.model.dao.OrderItemDao
import com.espresso.machine.model.entity.OrderItem

class OrderItemRepository(private val orderItemDao: OrderItemDao) {

    fun getOrderItemById(orderItemId: Int): OrderItem {
        return orderItemDao.getOrderItemById(orderItemId)
    }

    @WorkerThread
    suspend fun update(orderItem: OrderItem) {
        orderItemDao.update(orderItem)
    }

    @WorkerThread
    suspend fun insertCustom(orderItem: OrderItem) {
        orderItemDao.insertCustom(
            orderId = orderItem.orderId,
            foodItemId = orderItem.foodItemId,
            isUpdated = orderItem.isUpdated,
            size = orderItem.size,
            milkType = orderItem.milkType,
            itemPriceTotal = orderItem.itemPriceTotal,
            quantity = orderItem.quantity
        )
    }

    @WorkerThread
    suspend fun insert(orderItem: OrderItem) {
        orderItemDao.insert(orderItem)
    }

    @WorkerThread
    suspend fun delete(orderItem: OrderItem) {
        orderItemDao.delete(orderItem)
    }

}