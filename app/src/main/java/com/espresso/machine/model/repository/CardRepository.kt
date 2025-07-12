package com.espresso.machine.model.repository

import androidx.annotation.WorkerThread
import com.espresso.machine.model.dao.CardDao
import com.espresso.machine.model.entity.Card
import kotlinx.coroutines.flow.Flow

class CardRepository(private val cardDao: CardDao) {

    val allCards: Flow<List<Card>> = cardDao.getAll()

    @WorkerThread
    suspend fun insert(card: Card){
        cardDao.insert(card)
    }

    @WorkerThread
    suspend fun delete(card: Card){
        cardDao.delete(card)
    }

    fun getCardById(cardId: Int): Card {
        return cardDao.getCardById(cardId)
    }

    fun getCardByUserId(userId: Int): Card {
        return cardDao.getCardByUserId(userId)
    }

    fun findCardByCvc(cvc: Int): Card {
        return cardDao.findByCvv(cvc)
    }


}