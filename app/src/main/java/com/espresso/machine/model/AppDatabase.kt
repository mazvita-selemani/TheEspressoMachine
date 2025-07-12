package com.espresso.machine.model

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.DeleteColumn
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.espresso.machine.model.dao.CardDao
import com.espresso.machine.model.dao.FoodDao
import com.espresso.machine.model.dao.OrderDao
import com.espresso.machine.model.dao.OrderItemDao
import com.espresso.machine.model.dao.UserDao
import com.espresso.machine.model.entity.Card
import com.espresso.machine.model.entity.Food
import com.espresso.machine.model.entity.Order
import com.espresso.machine.model.entity.OrderItem
import com.espresso.machine.model.entity.User

@Database(
    entities = [User::class, Food::class, Card::class, Order::class, OrderItem::class],
    version = 9,
    autoMigrations = [AutoMigration(from = 8, to = 9)],
    exportSchema = true,
)
abstract class AppDatabase: RoomDatabase(){

    abstract fun userDao(): UserDao
    abstract fun foodDao(): FoodDao
    abstract fun cardDao(): CardDao
    abstract fun orderDao(): OrderDao
    abstract fun orderItemDao(): OrderItemDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "espresso_machine_db"
                ).allowMainThreadQueries().fallbackToDestructiveMigration().build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `Food` (
                `name` TEXT NOT NULL DEFAULT '',
                `image_resource_id` INTEGER NOT NULL,
                `id` INTEGER NOT NULL PRIMARY KEY,
                `category` TEXT NOT NULL DEFAULT '',
                `price` REAL NOT NULL
            )
            """.trimIndent()
        )
    }
}

val MIGRATION_4_5_ = object : Migration(4, 5) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Create the User table with the expected schema
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `User_expected` (
                `last_name` TEXT,
                `password` TEXT,
                `available_card_id` INTEGER,
                `id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                `email_address` TEXT,
                `first_name` TEXT
            )
        """.trimIndent())

        db.execSQL("""
            INSERT INTO `User_expected` (id, first_name, last_name, email_address, password, available_card_id)
            SELECT id, first_name, last_name, email_address, password, available_card_id
            FROM `User`
        """)

        // Drop the old User table if it exists
        db.execSQL("DROP TABLE `User`")

        // Rename the expected User table to the correct name
        db.execSQL("ALTER TABLE `User_expected` RENAME TO `User`")
        }
}

@DeleteColumn(
    tableName = "order",
    columnName = "order_items"
)
class MyAutoMigration : AutoMigrationSpec