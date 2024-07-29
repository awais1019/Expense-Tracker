package com.example.expensetracker.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface TransactionDao {
    @Query("""
    SELECT * FROM transaction_table 
    WHERE type = :type 
    AND substr(dateTime, 4, 2) = :month 
    AND substr(dateTime, 7, 4) = :year 
    AND userId = :userId
""")
    fun getTransactionsByMonth(month: String, year: String, userId: Int, type: String): LiveData<List<TransactionEntity>>
    @Query("""
    SELECT SUM(amount) 
    FROM transaction_table 
    WHERE type = :type 
    AND substr(dateTime, 4, 2) = :month 
    AND substr(dateTime, 7, 4) = :year 
    AND userId = :userId
""")
    fun getTotalAmount(month: String, year: String, userId: Int, type: String): LiveData<Double>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity)

    @Update
    suspend fun updateTransaction(transaction: TransactionEntity)

    @Delete
    suspend fun deleteTransaction(transaction: TransactionEntity)
}
