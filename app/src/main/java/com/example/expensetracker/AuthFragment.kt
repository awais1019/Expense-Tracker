package com.example.expensetracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.expensetracker.databinding.FragmentAuthBinding


class AuthFragment : Fragment() {

    private lateinit var binding:FragmentAuthBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentAuthBinding.inflate(inflater, container, false)
        val navController=findNavController()
        binding.btnLogin.setOnClickListener()
        {
          navController.navigate(R.id.action_authFragment_to_loginFragment2)
        }
        binding.btnSignup.setOnClickListener()
        {
            navController.navigate(R.id.action_authFragment_to_signupFragment2)
        }

        return binding.root
    }





   
}