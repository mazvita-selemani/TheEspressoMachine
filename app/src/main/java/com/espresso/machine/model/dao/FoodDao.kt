package com.espresso.machine.model.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.espresso.machine.model.entity.Food
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {
    @Query("SELECT * FROM food")
    fun getAll(): Flow<List<Food>>

    @Query("SELECT * FROM food WHERE category = :category")
    fun getAllByCategory(category: String): Flow<List<Food>>

    @Query("SELECT * FROM food WHERE id IN (:foodIds)")
    fun getAllByIds(foodIds: IntArray): List<Food>

    @Query("SELECT * FROM food WHERE id = :foodId")
    fun getFoodItemById(foodId: Int): Food

    @Insert
    suspend fun insertAll(vararg food: Food)

    @Insert
    suspend fun insert(food: Food)

    @Delete
    suspend fun delete(food: Food)

    @Query("DELETE FROM food")
    suspend fun deleteAll()

}