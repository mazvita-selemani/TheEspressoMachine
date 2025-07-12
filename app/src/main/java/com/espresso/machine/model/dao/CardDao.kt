package com.espresso.machine.model.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.espresso.machine.model.entity.Card
import kotlinx.coroutines.flow.Flow

@Dao
interface CardDao {
    @Query("SELECT * FROM card")
    fun getAll(): Flow<List<Card>>

    @Query("SELECT * FROM card WHERE id IN (:cardIds)")
    fun getAllByIds(cardIds: IntArray): List<Card>

    @Query("SELECT * FROM card WHERE id = :cardId")
    fun getCardById(cardId: Int): Card

    @Query("SELECT * FROM card WHERE user_id = :userId LIMIT 1")
    fun getCardByUserId(userId: Int): Card

    @Query("SELECT * FROM card WHERE cvc = :cvc")
    fun findByCvv(cvc: Int): Card

    @Insert
    suspend fun insertAll(vararg cards: Card)

    @Insert
    suspend fun insert(card: Card)

    @Delete
    suspend fun delete(card: Card)
}