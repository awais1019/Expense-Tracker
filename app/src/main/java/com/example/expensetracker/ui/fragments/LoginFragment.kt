package com.example.expensetracker.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.expensetracker.R
import com.example.expensetracker.databinding.FragmentLoginBinding
import com.example.expensetracker.db.UserEntity
import com.example.expensetracker.viewModels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

     private lateinit var  binding: FragmentLoginBinding
     private lateinit var navController: NavController
     private  val loginViewModel: LoginViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentLoginBinding.inflate(inflater, container, false)
        initializeNavController()


        binding.btnLoginToAccount.setOnClickListener()
        {
            val user = getValues()
            if (user == null) {
                Toast.makeText(
                    requireContext(),
                    "Please enter username and password",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                lifecycleScope.launch {
                    val isConfirmed = loginViewModel.loginConfirmed(requireContext(), user.username, user.password)

                    if (isConfirmed) {

                        Toast.makeText(requireContext(), "Login Successful", Toast.LENGTH_SHORT)
                            .show()
                        navController.navigate(R.id.action_loginFragment2_to_homeFragment)

                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Wrong Password or Username",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        binding.textviewRegister.setOnClickListener()
        {
            navController.navigate(R.id.action_loginFragment2_to_signupFragment2)
        }


        return binding.root
    }
    private fun getValues():UserEntity?
     {
         if(binding.editTextUsername.text.isEmpty()||binding.editTextPassword.text.isEmpty())
         {
             return null
         }
         val username=binding.editTextUsername.text.toString()
         val password=binding.editTextPassword.text.toString()
         return UserEntity(username=username,password=password)
     }


    private fun initializeNavController()
    {
        navController=findNavController()
    }


}