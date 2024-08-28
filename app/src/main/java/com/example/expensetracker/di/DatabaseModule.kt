package com.example.expensetracker.di

import android.content.Context
import androidx.room.Room
import com.example.expensetracker.db.DatabaseClass
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {


    @Provides
    @Singleton
    fun getDataBase(@ApplicationContext context:Context):DatabaseClass
    {
        return Room.databaseBuilder(context,DatabaseClass::class.java,"Expense_app_database").build()
    }

    @Provides
    @Singleton
    fun getUserDao(databaseClass: DatabaseClass)=databaseClass.getUserDao()


    @Provides
    @Singleton
    fun getTransactionDao(databaseClass: DatabaseClass)=databaseClass.getTransactionDao()

}