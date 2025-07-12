package com.espresso.machine.model.repository

import androidx.annotation.WorkerThread
import com.espresso.machine.model.dao.FoodDao
import com.espresso.machine.model.entity.Food
import kotlinx.coroutines.flow.Flow

class FoodRepository (private val foodDao: FoodDao) {

    val allFoods: Flow<List<Food>> = foodDao.getAll()

    @WorkerThread
    suspend fun insert(food: Food) {
        foodDao.insert(food)
    }

    @WorkerThread
    suspend fun delete(food: Food) {
        foodDao.delete(food)
    }

    @WorkerThread
    suspend fun deleteAll() {
    foodDao.deleteAll()
    }

    @WorkerThread
    suspend fun insertAll(vararg foods: Food) {
        foodDao.insertAll(*foods)
    }

    fun getFoodItemById(foodId: Int): Food {
        return foodDao.getFoodItemById(foodId)
    }

    fun getAllByCategory(category: String): Flow<List<Food>> {
        return foodDao.getAllByCategory(category)
    }

    fun getAllByIds(foodIds: IntArray): List<Food> {
        return foodDao.getAllByIds(foodIds)
    }

}