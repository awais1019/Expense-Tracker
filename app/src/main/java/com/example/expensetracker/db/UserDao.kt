package com.example.expensetracker.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update


@Dao
interface UserDao {

    @Query("SELECT * FROM user_table WHERE username = :username")
    suspend fun getUserByName(username:String): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity):Long

    @Delete
    suspend fun deleteUser(user: UserEntity)


    @Update
    suspend fun updateUser(user: UserEntity)


}