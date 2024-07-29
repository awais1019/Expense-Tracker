package com.example.expensetracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.expensetracker.adapter.RvAdapter
import com.example.expensetracker.databinding.FragmentHomeBinding
import com.example.expensetracker.db.DatabaseClass
import com.example.expensetracker.viewModels.LoginViewModelFactory
import com.example.expensetracker.viewModels.TransactionViewModel
import com.example.expensetracker.viewModels.TransactionViewModelFactory
import java.util.Date


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter1: RvAdapter
    private lateinit var transactionViewModel: TransactionViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val database = DatabaseClass.getDatabaseInstance(requireContext())
        val userDao = database.getUserDao()
        val transactionDao = database.getTransactionDao()
        val repositoryClass = RepositoryClass(userDao, transactionDao)
        val viewModelFactory = TransactionViewModelFactory(repositoryClass)
        transactionViewModel = ViewModelProvider(this, viewModelFactory)[TransactionViewModel::class.java]

        setObservers()
        setListeners()
        fetchTransactions("Expense")
        fetchIncome("Income")
        fetchIncome("Expense")

        return binding.root

    }

    private fun setObservers() {

        transactionViewModel.currentDay.observe(viewLifecycleOwner) {
            binding.textViewDay.text = it
        }
        transactionViewModel.currentDate.observe(viewLifecycleOwner) {
            binding.textDate.text = it

            transactionViewModel.transaction.observe(viewLifecycleOwner) { transaction ->
                adapter1 = RvAdapter(transaction)
                binding.recycleView.adapter = adapter1
            }

            transactionViewModel.totalIncome.observe(viewLifecycleOwner) { money ->
                binding.textTotalMoneyAddedValue.text = money.toString()
            }
            transactionViewModel.totalExpense.observe(viewLifecycleOwner) { money ->
                binding.textExpenseMoneyValue.text = money.toString()
            }
            transactionViewModel.remainingIncome.observe(viewLifecycleOwner) { money ->
                run {
                    binding.textRemainingMoneyValue.text = money.toString()
                }
            }
        }

    }

    private fun setListeners()
    {
        binding.textViewExpenses.setOnClickListener()
        {
            fetchTransactions("Expense")
        }
        binding.textViewDeposits.setOnClickListener()
        {
            fetchTransactions("Income")
        }
    }

    private fun fetchTransactions(s: String) {
        transactionViewModel.getTransactionsByMonth(
            transactionViewModel.getCurrentMonth(),
            transactionViewModel.getCurrentYear(),
            requireContext(),s
        )
    }
    private fun fetchIncome(s:String) {

        when (s) {
            "Income" -> {
                transactionViewModel.getIncomeTotal(
                    transactionViewModel.getCurrentMonth(),
                    transactionViewModel.getCurrentYear(),
                    requireContext(),
                    s
                )
            }

            "Expense" -> {
                transactionViewModel.getExpenseTotal(
                    transactionViewModel.getCurrentMonth(),
                    transactionViewModel.getCurrentYear(),
                    requireContext(), s
                )
            }
        }
    }
}




