package com.example.expensetracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController


class AuthFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_auth, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btn1=view.findViewById<Button>(R.id.loginFragment)
        val btn2=view.findViewById<Button>(R.id.signupFragment)


        btn1.setOnClickListener()
        {
            findNavController().navigate(R.id.action_authFragment_to_loginFragment)
        }

        btn2.setOnClickListener()
        {
            findNavController().navigate(R.id.action_authFragment_to_signupFragment)
        }
    }



   
}