package com.espresso.machine.model.repository

import androidx.annotation.WorkerThread
import com.espresso.machine.model.dao.OrderDao
import com.espresso.machine.model.entity.Order
import com.espresso.machine.model.entity.OrderStatus
import com.espresso.machine.model.entity.OrderWithOrderItems

class OrderRepository(private val orderDao: OrderDao) {

    @WorkerThread
    suspend fun insert(order: Order) {
        orderDao.insert(order)
    }

    @WorkerThread
    suspend fun update(order: Order){
        orderDao.update(order)
    }

    @WorkerThread
    suspend fun updateOrderStatus(orderId: Int, orderStatus: OrderStatus){
        orderDao.updateOrderStatus(orderId, orderStatus)
    }

    fun getOpenOrderForUser(userId: Int): Order? {
        return orderDao.getOpenOrderForUser(userId)
    }

    fun getOrderWithOrderItems(orderId : Int): OrderWithOrderItems{
        return orderDao.getOrderWithOrderItems(orderId)
    }

    fun getOrderById(orderId: Int): Order {
        return orderDao.getOrderById(orderId)
    }
}