package com.example.expensetracker.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.expensetracker.R
import com.example.expensetracker.databinding.FragmentTransactionBinding
import com.example.expensetracker.db.TransactionEntity
import com.example.expensetracker.viewModels.TransactionViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransactionFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentTransactionBinding
    private val transactionViewModel: TransactionViewModel by viewModels()
    private val args: TransactionFragmentArgs by navArgs()
    private val transaction: TransactionEntity? by lazy { args.transactionEntity }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTransactionBinding.inflate(inflater, container, false)
        setAdapterForDropDown()
        populateFields()
        setupListeners()
        return binding.root
    }



    private fun setupListeners() {
        binding.btnAddTransaction.setOnClickListener {
            handleAddTransaction()
        }
    }

    private fun handleAddTransaction() {

        val title = binding.editTextPurpose.text.toString()
        val amount = binding.editAmount.text.toString()
        val type = binding.autoCompleteTextView.text.toString()

        if(transaction!=null)
        {
            if(transactionViewModel.updateValues(transaction!!.id,title,amount,type,requireContext()))
            {
                showSuccessToast("Transaction Updated")
                clearInputFields()
            }
            else
            {
                observeError()
            }
        }
        else
        {
            if (transactionViewModel.insertValues(title, amount, type, requireContext())) {
                showSuccessToast("Transaction Added")
                clearInputFields()
            } else {
                observeError()
            }
        }

    }

    private fun showSuccessToast(msg:String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    private fun clearInputFields() {
        binding.editTextPurpose.text?.clear()
        binding.editAmount.text?.clear()
    }

    private fun setAdapterForDropDown()
    {
        val list= arrayListOf("Income","Expense")
        val adapter = ArrayAdapter(requireContext(), R.layout.drop_down_text, list)
        binding.autoCompleteTextView.setAdapter(adapter)
    }

    private fun observeError() {
        transactionViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    private fun populateFields() {
        transaction?.let {
            binding.editTextPurpose.setText(it.title)
            binding.editAmount.setText(it.amount.toString())
            binding.autoCompleteTextView.setText(it.type, false)
        }
    }


}
