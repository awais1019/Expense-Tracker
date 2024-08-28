package com.example.expensetracker

import androidx.lifecycle.LiveData
import com.example.expensetracker.db.TransactionDao
import com.example.expensetracker.db.TransactionEntity
import com.example.expensetracker.db.UserDao
import com.example.expensetracker.db.UserEntity
import javax.inject.Inject

class RepositoryClass @Inject constructor(private val userDao: UserDao, private val transactionDao: TransactionDao) {

    suspend fun getUserByName(username: String): UserEntity?
    {
        return userDao.getUserByName(username)
    }
    suspend fun deleteUser(user: UserEntity)
    {
        return userDao.deleteUser(user)
    }
    suspend fun insertUser(user: UserEntity):Long
    {
        return userDao.insertUser(user)
    }
    suspend fun updateUser(user: UserEntity)
    {
        return userDao.updateUser(user)
    }




    suspend fun deleteTransaction(transaction: TransactionEntity)
    {
        return transactionDao.deleteTransaction(transaction)
    }
    suspend fun insertTransaction(transaction: TransactionEntity)
    {
        return transactionDao.insertTransaction(transaction)
    }
    suspend fun updateTransaction(transaction: TransactionEntity)
    {
        return transactionDao.updateTransaction(transaction)
    }

    fun getTransactionsByMonth(month: String, year: String, userId: Int, type: String): LiveData<List<TransactionEntity>>
    {
        return transactionDao.getTransactionsByMonth(month, year, userId, type)
    }
    fun getTotalAmount(month: String, year: String, userId: Int, type: String): LiveData<Double>
    {
        return transactionDao.getTotalAmount(month, year, userId, type)
    }

    fun  getMonthAndYears(userId:Int):LiveData<List<String>>
    {
        return transactionDao.getStoredMonthAndYear(userId)
    }

    suspend fun deleteFullMonthRecord(id:Int,month:String,year:String)
    {
        transactionDao.deleteFullMonthRecord(id,month,year)
    }




}