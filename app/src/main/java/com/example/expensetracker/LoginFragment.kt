package com.example.expensetracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.expensetracker.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {

     lateinit var  binding: FragmentLoginBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentLoginBinding.inflate(inflater, container, false)
        val navController=findNavController()
        binding.textviewRegister.setOnClickListener()
        {
            navController.navigate(R.id.action_loginFragment2_to_homeFragment)
        }
        return binding.root
    }


}