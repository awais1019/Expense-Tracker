package com.example.expensetracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
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
    private lateinit var signUpViewModel: SignUpViewModel
    private lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentSignupBinding.inflate(inflater, container, false)
        setViewModels()
        initializeNavController()
        observer()
        clickListeners()
        return binding.root
    }


    private fun observer()
    {
       signUpViewModel.error.observe(viewLifecycleOwner)
       {
           Toast.makeText(requireContext(),it,Toast.LENGTH_SHORT).show()
       }
    }
    private fun clickListeners()
    {
        binding.btnCreateAccount.setOnClickListener()
        {
            createUser()
        }
        binding.textviewLogin.setOnClickListener()
        {
            navController.navigate(R.id.action_signupFragment2_to_loginFragment2)
        }
    }
    private fun initializeNavController() {
        navController=findNavController()

    }
    private fun createUser()
    {
        val password = binding.editTextPassword.text.toString()
        val userName = binding.editTextUsername.text.toString()
        val user = UserEntity(username = userName, password = password)
        val confirmPassword=binding.editTextConfirmPassword.text.toString()
        if(signUpViewModel.insertUserIntoDatabase(user,confirmPassword))
        {
            clearFields()

        }

    }

    private fun clearFields() {
        binding.editTextPassword.text.clear()
        binding.editTextPassword.text.clear()
        binding.editTextConfirmPassword.text.clear()
    }

    private fun setViewModels() {
        val database= DatabaseClass.getDatabaseInstance(requireContext())
        val userDao=database.getUserDao()
        val transactionDao=database.getTransactionDao()
        val repositoryClass=RepositoryClass(userDao,transactionDao)
        val viewModelFactory= SignUpViewModelFactory(repositoryClass)
        signUpViewModel= ViewModelProvider(this,viewModelFactory)[SignUpViewModel::class.java]
    }

}