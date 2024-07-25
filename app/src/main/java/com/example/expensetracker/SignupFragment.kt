package com.example.expensetracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.expensetracker.databinding.FragmentSignupBinding


class SignupFragment : Fragment() {
    lateinit var binding:FragmentSignupBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentSignupBinding.inflate(inflater, container, false)
        val navController=findNavController()
        binding.textviewLogin.setOnClickListener()
        {
            navController.navigate(R.id.action_signupFragment2_to_loginFragment2)
        }
        return binding.root
    }

}