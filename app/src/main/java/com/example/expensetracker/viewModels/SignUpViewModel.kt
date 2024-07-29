package com.example.expensetracker.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.RepositoryClass
import com.example.expensetracker.db.UserEntity
import kotlinx.coroutines.launch

class SignUpViewModel(private val repositoryClass: RepositoryClass) : ViewModel() {

    private val _error = MutableLiveData<String>()
    val error get() = _error



    private fun insertUser(user: UserEntity) {
        viewModelScope.launch {
            repositoryClass.insertUser(user)
        }
    }

    fun insertUserIntoDatabase(user: UserEntity, confirmPassword: String): Boolean {

        if (user.username.isEmpty()) {
            _error.value = "Please enter Username"
            return false
        }

        if (user.password.isEmpty()) {
            _error.value = "Please enter Password"
            return false
        }

        if (confirmPassword.isEmpty()) {
            _error.value = "Please enter Confirm Password"
            return false
        }

        if (!checkConfirmPassword(user.password, confirmPassword)) {
            _error.value = "Password does not match"
            return false
        }

        insertUser(user)
        _error.value = "User Added"
        return true
    }

    private fun checkConfirmPassword(password: String, confirmPassword: String): Boolean {
        return password == confirmPassword
    }
}
