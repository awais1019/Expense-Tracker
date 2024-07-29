package com.example.expensetracker.viewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.expensetracker.RepositoryClass
import com.example.expensetracker.db.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoginViewModel(private val repositoryClass: RepositoryClass):ViewModel() {


    private suspend fun getUserByName(username: String): UserEntity? {

            return withContext(Dispatchers.IO) {
                repositoryClass.getUserByName(username)
            }
        }



    suspend fun loginConfirmed(context: Context, username: String, password: String): Boolean {
        val entity = getUserByName(username)
        return if (entity?.password == password) {
            saveUserId(context, entity.id)
            true
        } else {
            false
        }
    }

    private fun saveUserId(context: Context, userId: Int) {
        val sharedPref = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putInt("user_id", userId)
            apply()
        }
    }


}