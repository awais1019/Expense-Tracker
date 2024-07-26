package com.example.expensetracker.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.RepositoryClass
import com.example.expensetracker.db.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignUpViewModel(private val repositoryClass: RepositoryClass):ViewModel() {


    fun insertUser(user: UserEntity)
    {
        viewModelScope.launch {
            repositoryClass.insertUser(user)
        }

    }




}