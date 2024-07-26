package com.example.expensetracker.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.expensetracker.RepositoryClass

class SignUpViewModelFactory(private val repositoryClass: RepositoryClass) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
            return SignUpViewModel(repositoryClass) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
