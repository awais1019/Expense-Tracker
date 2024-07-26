package com.example.expensetracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.expensetracker.databinding.FragmentSignupBinding
import com.example.expensetracker.db.DatabaseClass
import com.example.expensetracker.db.UserEntity
import com.example.expensetracker.viewModels.LoginViewModel
import com.example.expensetracker.viewModels.LoginViewModelFactory
import com.example.expensetracker.viewModels.SignUpViewModel
import com.example.expensetracker.viewModels.SignUpViewModelFactory


class SignupFragment : Fragment() {
    private lateinit var binding:FragmentSignupBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentSignupBinding.inflate(inflater, container, false)
        val database= DatabaseClass.getDatabaseInstance(requireContext())
        val userDao=database.getUserDao()
        val transactionDao=database.getTransactionDao()
        val repositoryClass=RepositoryClass(userDao,transactionDao)
        val viewModelFactory= SignUpViewModelFactory(repositoryClass)
        val signUpViewModel= ViewModelProvider(this,viewModelFactory)[SignUpViewModel::class.java]
        val navController=findNavController()
        binding.btnCreateAccount.setOnClickListener()
        {
            if(binding.editTextPassword.text.isEmpty()||binding.editTextUsername.text.isEmpty())
            {
                Toast.makeText(requireContext(),"Please enter username and password",Toast.LENGTH_SHORT).show()
            }
            else {
                val password = binding.editTextPassword.text.toString()
                val userName = binding.editTextUsername.text.toString()
                val user = UserEntity(username = userName, password = password)
                signUpViewModel.insertUser(user)
                Toast.makeText(requireContext(), "User Added", Toast.LENGTH_SHORT).show()
            }
        }


        binding.textviewLogin.setOnClickListener()
        {
            navController.navigate(R.id.action_signupFragment2_to_loginFragment2)
        }
        return binding.root
    }

}