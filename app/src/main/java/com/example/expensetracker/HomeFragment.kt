package com.example.expensetracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.expensetracker.adapter.RvAdapter
import com.example.expensetracker.databinding.FragmentHomeBinding
import com.example.expensetracker.db.DatabaseClass
import com.example.expensetracker.db.TransactionEntity
import com.example.expensetracker.viewModels.TransactionViewModel
import com.example.expensetracker.viewModels.TransactionViewModelFactory

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: RvAdapter
    private lateinit var transactionViewModel: TransactionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        setUptViewModel()
        setupRecyclerView()
        observeViewModel()
        setListeners()
        fetchTransactions("Income")
        fetchTransactions("Expense")

        return binding.root
    }

    private fun setUptViewModel() {
        val database = DatabaseClass.getDatabaseInstance(requireContext())
        val userDao = database.getUserDao()
        val transactionDao = database.getTransactionDao()
        val repositoryClass = RepositoryClass(userDao, transactionDao)
        val viewModelFactory = TransactionViewModelFactory(repositoryClass)
        transactionViewModel = ViewModelProvider(this, viewModelFactory)[TransactionViewModel::class.java]
    }

    private fun setupRecyclerView(list: List<TransactionEntity> =emptyList()) {
        adapter = RvAdapter(list)
        binding.recycleView.adapter = adapter
    }

    private fun observeViewModel() {
        transactionViewModel.currentDay.observe(viewLifecycleOwner) {
            binding.textViewDay.text = it
        }
        transactionViewModel.currentDate.observe(viewLifecycleOwner) {
            binding.textDate.text = it
        }

        transactionViewModel.transaction.observe(viewLifecycleOwner) { transactions ->
            if (transactions.isEmpty()) {
                binding.textViewNoRecords.visibility = View.VISIBLE
                setupRecyclerView()
            } else {
                binding.textViewNoRecords.visibility = View.GONE
                setupRecyclerView(transactions)

            }
        }
        transactionViewModel.transaction2.observe(viewLifecycleOwner) { transactions ->
            if (transactions.isEmpty()) {
                binding.textViewNoRecords.visibility = View.VISIBLE
                setupRecyclerView()
            } else {
                binding.textViewNoRecords.visibility = View.GONE
                setupRecyclerView(transactions)

            }
        }

        transactionViewModel.totalIncome.observe(viewLifecycleOwner) { money ->
            binding.textTotalMoneyAddedValue.text = money.toString()
        }

        transactionViewModel.totalExpense.observe(viewLifecycleOwner) { money ->
            binding.textExpenseMoneyValue.text = money.toString()
        }

        transactionViewModel.remainingIncome.observe(viewLifecycleOwner) { money ->
            binding.textRemainingMoneyValue.text = money.toString()
        }
    }

    private fun setListeners() {
        binding.textViewExpenses.setOnClickListener {
            fetchTransactions("Expense")
        }
        binding.textViewDeposits.setOnClickListener {
            fetchTransactions("Income")
        }
    }

    private fun fetchTransactions(type: String) {
        transactionViewModel.getTransactionsByMonth(
            transactionViewModel.getCurrentMonth(),
            transactionViewModel.getCurrentYear(),
            requireContext(),
            type
        )
    }



}
