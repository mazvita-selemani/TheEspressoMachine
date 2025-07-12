package com.espresso.machine.model.repository

import androidx.annotation.WorkerThread
import com.espresso.machine.model.dao.UserDao
import com.espresso.machine.model.entity.User
import kotlinx.coroutines.flow.Flow

/**
 * A repository class abstracts access to multiple data sources.
 * A Repository class provides a clean API for data access to the rest of the application.
 * A Repository manages queries and allows you to use multiple backends.
 */
class UserRepository(private val userDao: UserDao) {

    val allUsers: Flow<List<User>> = userDao.getAll()

    @WorkerThread
    suspend fun insert(user: User){
        userDao.insert(user)
    }

    fun getUserById(userId: Int): User {
        return userDao.getUserById(userId)
    }

    fun findUserByEmail(email: String): User{
        return userDao.findByEmailAddress(email)
    }


}