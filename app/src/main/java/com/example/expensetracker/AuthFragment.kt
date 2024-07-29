package com.example.expensetracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.NavAction
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.expensetracker.databinding.FragmentAuthBinding


class AuthFragment : Fragment() {

    private lateinit var binding:FragmentAuthBinding
    private lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentAuthBinding.inflate(inflater, container, false)
        initializeNavController()
        setListeners()

        return binding.root
    }

    private fun setListeners() {

        binding.btnLogin.setOnClickListener()
        {
            performAction(R.id.action_authFragment_to_loginFragment2)
        }
        binding.btnSignup.setOnClickListener()
        {
            performAction(R.id.action_authFragment_to_signupFragment2)
        }
    }
    private fun performAction(action:Int)
    {
            navController.navigate(action)
    }

    private fun initializeNavController()
    {
        navController=findNavController()
    }





   
}