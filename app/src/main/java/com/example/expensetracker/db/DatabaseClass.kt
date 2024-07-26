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

     abstract fun getUserDao():UserEntity

     abstract fun getTransactionDao():TransactionEntity

     companion object{
         @Volatile
         var instance:DatabaseClass?=null
         fun getDatabaseInstance(context: Context):DatabaseClass{
            return instance?:synchronized(this){
                val newInstance= Room.databaseBuilder(context.applicationContext,
                    DatabaseClass::class.java,
                    "Expense_app_database"
                    ).build()
                instance=newInstance
                newInstance
            }
         }
     }

}