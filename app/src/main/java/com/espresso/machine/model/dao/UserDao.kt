package com.espresso.machine.model.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.espresso.machine.model.entity.User
import kotlinx.coroutines.flow.Flow

/**
 * provides the methods that the rest of the app uses to interact with data in the user table
 */
// TODO search about target entity
@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): Flow<List<User>>

    @Query("SELECT * FROM user WHERE id IN (:userIds)")
    fun getAllByIds(userIds: IntArray): List<User>

    @Query("SELECT * FROM user WHERE id = :userId")
    fun getUserById(userId: Int): User

    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " + "last_name LIKE :last LIMIT 1")
    fun findByName(first: String, last:String): User

    @Query("SELECT * FROM user WHERE email_address =:email LIMIT 1")
    fun findByEmailAddress(email:String): User

    @Insert
    suspend fun insertAll(vararg users: User)

    @Insert
    suspend fun insert(user: User)

    @Delete
    suspend fun delete(user: User)
}