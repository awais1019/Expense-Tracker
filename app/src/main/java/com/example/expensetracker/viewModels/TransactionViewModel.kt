package com.example.expensetracker.viewModels

import android.content.Context
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

    val currentDay: LiveData<String> = liveData {
        emit(getCurrentDay())
    }

    val currentDate: LiveData<String> = liveData {
        emit(getCurrentDate())
    }


    private var _totalMonthAndYears=MutableLiveData<List<String>>()
    val saveYearMonth get() = _totalMonthAndYears


    fun getStoredMonthAndYear(context: Context) {
        viewModelScope.launch {
            repository.getMonthAndYears(getUserId(context)).observeForever { monthYearList ->
                _totalMonthAndYears.postValue(monthYearList)
            }
        }
    }

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _totalIncome = MutableLiveData<Double>().apply { value = 0.0 }
    val totalIncome: LiveData<Double> get() = _totalIncome

    private val _totalExpense = MutableLiveData<Double>().apply { value = 0.0 }
    val totalExpense: LiveData<Double> get() = _totalExpense

    private val _remainingIncome = MutableLiveData<Double>().apply { value = 0.0 }
    val remainingIncome: LiveData<Double> get() = _remainingIncome

    private val _transaction = MutableLiveData<List<TransactionEntity>>().apply { value = emptyList() }
    val transaction: LiveData<List<TransactionEntity>> get() = _transaction

    private val _transaction2 = MutableLiveData<List<TransactionEntity>>().apply { value = emptyList() }
    val transaction2: LiveData<List<TransactionEntity>> get() = _transaction2

    fun getTransactionsByMonth(month: String, year: String, context: Context, type: String) {
        val userId = getUserId(context)
        repository.getTransactionsByMonth(month, year, userId, type).observeForever { transactions ->
            if (type == "Income") {
                _transaction.value = transactions ?: emptyList()
                _totalIncome.value = calculateTotal(transactions)
            } else if (type == "Expense") {
                _transaction2.value = transactions ?: emptyList()
                _totalExpense.value = calculateTotal(transactions)
            }
            updateRemainingIncome()
        }
    }

    private fun calculateTotal(transactions: List<TransactionEntity>?): Double {
        return transactions?.sumOf { it.amount } ?: 0.0
    }

    fun insertValues(title: String, amount: String, type: String, context: Context): Boolean {

        if(!performValidations(title,amount,type,context))
        {
          return false
        }
        else
        {
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

    }

    fun updateValues(id:Int,title: String, amount: String, type: String, context: Context):Boolean
    {
        if(!performValidations(title,amount,type,context)) {
            return false
        }
        else
        {
            val convertedAmount = convertStringToDouble(amount)
            val currentDateTime = getCurrentTimeDate()
            val userId = getUserId(context)
            val transaction = TransactionEntity(
                id=id,
                title = title,
                amount = convertedAmount,
                type = type,
                userId = userId,
                dateTime = currentDateTime
            )
            updateTransaction(transaction)
            return true
        }
    }



    private fun performValidations(title: String, amount: String, type: String,context: Context): Boolean {
        if (title.isEmpty()) {
            _error.value = "Title cannot be empty"
            return false
        }

        val amountValue = amount.toDoubleOrNull()
        if (amountValue == null || amountValue <= 0) {
            _error.value = "Amount cannot be empty or less than or equal to zero"
            return false
        }
        if(type.isEmpty())
        {
            _error.value = "Must select a transaction type"
            return false
        }

        val sharePref=context.getSharedPreferences("remaining_amount",Context.MODE_PRIVATE)
        val getAmount=sharePref.getFloat("remaining_amount",0.0f)
        if (type == "Expense" && amountValue > getAmount) {
            _error.value = "Insufficient Balance"
            return false
        }

        return true
    }

    private fun convertStringToDouble(string: String): Double {
        return string.toDoubleOrNull() ?: 0.0
    }

    private fun getCurrentDay(): String {
        val format = SimpleDateFormat("EEEE", Locale.getDefault())
        return format.format(Date())
    }

    private fun getCurrentDate(): String {
        val format = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return format.format(Date())
    }

    private fun getUserId(context: Context): Int {
        val sharedPref = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        return sharedPref.getInt("user_id", -1)
    }

      fun saveRemainingAmount(context: Context){
        val remainingAmount = remainingIncome.value ?: 0.0
        val currentMonth=getCurrentMonth()
        if(remainingAmount>0)
        {
            val sharedPref = context.getSharedPreferences("remaining_amount", Context.MODE_PRIVATE)
            sharedPref.edit().putFloat("remaining_amount", remainingAmount.toFloat()).apply()
            sharedPref.edit().putString("month",currentMonth).apply()
        }


    }

     fun getSaveIncome(context: Context)
    {
        val sharePref=context.getSharedPreferences("remaining_amount",Context.MODE_PRIVATE)
        val getMonth=sharePref.getString("month","")
        if(getCurrentMonth()!=getMonth)
        {
            val getAmount=sharePref.getFloat("remaining_amount",0.0f)
            insertValues(title="Previous Month Savings",amount=getAmount.toString(),type="Income",context)
            saveRemainingAmount(context)
        }


    }

    private fun getCurrentTimeDate(): String {
        val format = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        return format.format(Date())
    }

    private fun insertTransaction(transaction: TransactionEntity) {
        viewModelScope.launch {
            repository.insertTransaction(transaction)
        }
    }

    private fun updateTransaction(transaction: TransactionEntity) {
        viewModelScope.launch {
            repository.updateTransaction(transaction)
        }
    }

    fun deleteTransaction(transaction: TransactionEntity) {
        viewModelScope.launch {
            repository.deleteTransaction(transaction)
        }
    }

    private fun updateRemainingIncome(){
        val income = _totalIncome.value ?: 0.0
        val expense = _totalExpense.value ?: 0.0
        _remainingIncome.value = income - expense
    }


    fun getCurrentMonth(): String {
        val format = SimpleDateFormat("MM", Locale.getDefault())
        return format.format(Date())
    }

    fun getCurrentYear(): String {
        val format = SimpleDateFormat("yyyy", Locale.getDefault())
        return format.format(Date())
    }

    fun deleteWholeMonthRecord(month: String,year: String,context: Context)
    {
        val id=getUserId(context)
        viewModelScope.launch {
            repository.deleteFullMonthRecord(id,month,year)
        }
    }
}
