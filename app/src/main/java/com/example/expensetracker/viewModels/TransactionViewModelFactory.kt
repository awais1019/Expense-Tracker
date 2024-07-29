package com.example.expensetracker.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.expensetracker.RepositoryClass

class TransactionViewModelFactory(private val repositoryClass:RepositoryClass):ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TransactionViewModel::class.java)) {
                return TransactionViewModel(repositoryClass) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }


}