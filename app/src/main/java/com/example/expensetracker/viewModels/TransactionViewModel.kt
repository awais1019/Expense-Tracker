package com.example.expensetracker.viewModels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.RepositoryClass
import com.example.expensetracker.db.TransactionEntity
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TransactionViewModel(private val repository: RepositoryClass) : ViewModel() {

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    val currentDay: LiveData<String> = liveData {
        emit(getCurrentDay())
    }

    val currentDate: LiveData<String> = liveData {
        emit(getCurrentDate())
    }

    private val _totalIncome = MutableLiveData<Double>()
    val totalIncome: LiveData<Double> get() = _totalIncome

    private val _totalExpense = MutableLiveData<Double>()
    val totalExpense: LiveData<Double> get() = _totalExpense

    private val _remainingIncome = MutableLiveData<Double>()
    val remainingIncome: LiveData<Double> get() = _remainingIncome

    private val _transaction = MutableLiveData<List<TransactionEntity>>()
    val transaction: LiveData<List<TransactionEntity>> get() = _transaction

    init {

        totalIncome.observeForever { income ->
            val expense = _totalExpense.value ?: 0.0
            _remainingIncome.value = calculateRemaining(income, expense)
        }

        totalExpense.observeForever { expense ->
            val income = _totalIncome.value ?: 0.0
            _remainingIncome.value = calculateRemaining(income, expense)
        }
    }

    private fun calculateRemaining(totalIncome: Double, totalExpense: Double): Double {
        return totalIncome - totalExpense
    }

    fun getTransactionsByMonth(month: String, year: String, context: Context, type: String) {
        val userId = getUserId(context)
        val liveDataFromRepo = repository.getTransactionsByMonth(month, year, userId, type)
        liveDataFromRepo.observeForever { transactions ->
            _transaction.postValue(transactions)
        }
    }

    fun getIncomeTotal(month: String, year: String, context: Context, type: String) {
        val userId = getUserId(context)
        val liveDataFromRepo = repository.getTotalAmount(month, year, userId, type)
        liveDataFromRepo.observeForever { total ->
            _totalIncome.postValue(total)
        }
    }

    fun getExpenseTotal(month: String, year: String, context: Context, type: String) {
        val userId = getUserId(context)
        val liveDataFromRepo = repository.getTotalAmount(month, year, userId, type)
        liveDataFromRepo.observeForever { total ->
            _totalExpense.postValue(total)
        }
    }

    private fun insertTransaction(transaction: TransactionEntity) {
        viewModelScope.launch {
            repository.insertTransaction(transaction)
        }
    }

    fun updateTransaction(transaction: TransactionEntity) {
        viewModelScope.launch {
            repository.updateTransaction(transaction)
        }
    }

    fun deleteTransaction(transaction: TransactionEntity) {
        viewModelScope.launch {
            repository.deleteTransaction(transaction)
        }
    }

    fun insertValues(title: String, amount: String, type: String, context: Context): Boolean {
        if (title.isEmpty()) {
            _error.value = "Title cannot be empty"
            return false
        }

        if ((amount.isEmpty())) {
            _error.value = "Amount cannot be empty"
            return false
        }
        val convertedAmount = convertStringToDouble(amount)
        val currentDateTime = getCurrentTimeDate()
        val userId = getUserId(context)
        val transaction = TransactionEntity(
            title = title,
            amount = convertedAmount,
            type = type,
            userId = userId,
            dateTime = currentDateTime
        )
        insertTransaction(transaction)
        return true
    }

    private fun convertStringToDouble(string: String): Double {
        return string.toDoubleOrNull() ?: 0.0
    }

    private fun getCurrentTimeDate(): String {
        val format = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        return format.format(Date())
    }

    private fun getUserId(context: Context): Int {
        val sharedPref = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        return sharedPref.getInt("user_id", -1)
    }

    private fun getCurrentDay(): String {
        val format = SimpleDateFormat("EEEE", Locale.getDefault())
        return format.format(Date())
    }

    private fun getCurrentDate(): String {
        val format = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return format.format(Date())
    }

    fun getCurrentMonth(): String {
        val format = SimpleDateFormat("MM", Locale.getDefault())
        return format.format(Date())
    }

    fun getCurrentYear(): String {
        val format = SimpleDateFormat("yyyy", Locale.getDefault())
        return format.format(Date())
    }
}

