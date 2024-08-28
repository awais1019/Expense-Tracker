package com.example.expensetracker.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(
    entities = [UserEntity::class,TransactionEntity::class],
    version = 1
)
 abstract  class DatabaseClass: RoomDatabase() {

     abstract fun getUserDao():UserDao
     abstract fun getTransactionDao():TransactionDao

}