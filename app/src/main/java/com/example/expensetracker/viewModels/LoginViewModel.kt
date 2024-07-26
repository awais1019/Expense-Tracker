package com.example.expensetracker.viewModels

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



   suspend  fun loginConfirmed(username: String, password: String): Boolean {
      val entity=getUserByName(username)
       return entity?.password == password

    }


}