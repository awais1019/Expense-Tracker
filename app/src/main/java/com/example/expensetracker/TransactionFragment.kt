package com.example.expensetracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.expensetracker.databinding.FragmentTransactionBinding
import com.example.expensetracker.db.DatabaseClass
import com.example.expensetracker.viewModels.TransactionViewModel
import com.example.expensetracker.viewModels.TransactionViewModelFactory

class TransactionFragment : Fragment() {
    private lateinit var binding: FragmentTransactionBinding
    private lateinit var transactionViewModel: TransactionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTransactionBinding.inflate(inflater, container, false)
        setupViewModel()
        setupListeners()
        return binding.root
    }

    private fun setupViewModel() {
        val database = DatabaseClass.getDatabaseInstance(requireContext())
        val userDao = database.getUserDao()
        val transactionDao = database.getTransactionDao()
        val repositoryClass = RepositoryClass(userDao, transactionDao)
        val viewModelFactory = TransactionViewModelFactory(repositoryClass)
        transactionViewModel = ViewModelProvider(this, viewModelFactory)[TransactionViewModel::class.java]
    }

    private fun setupListeners() {
        binding.btnAddTransaction.setOnClickListener {
            handleAddTransaction()
        }
    }

    private fun handleAddTransaction() {
        val title = binding.editTextPurpose.text.toString()
        val amount = binding.editAmount.text.toString()
        val type = binding.transactionTypeSpinner.selectedItem.toString()
        if (transactionViewModel.insertValues(title, amount, type, requireContext())) {
            showSuccessToast()
            clearInputFields()
        } else {
            observeError()
        }
    }

    private fun showSuccessToast() {
        Toast.makeText(requireContext(), "Transaction Added", Toast.LENGTH_SHORT).show()
    }

    private fun clearInputFields() {
        binding.editTextPurpose.text?.clear()
        binding.editAmount.text?.clear()
        binding.transactionTypeSpinner.setSelection(0)
    }

    private fun observeError() {
        transactionViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
        }
    }
}
