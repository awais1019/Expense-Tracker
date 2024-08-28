package com.example.expensetracker.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.expensetracker.R
import com.example.expensetracker.RepositoryClass
import com.example.expensetracker.adapter.RvAdapter
import com.example.expensetracker.databinding.FragmentHomeBinding
import com.example.expensetracker.db.DatabaseClass
import com.example.expensetracker.db.TransactionEntity
import com.example.expensetracker.viewModels.TransactionViewModel
import com.example.expensetracker.viewModels.TransactionViewModelFactory
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: RvAdapter
    private val transactionViewModel: TransactionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        setupRecyclerView()
        observeViewModel()
        setFloatingButtonListener()
        setListeners()
        fetchTransactions("Expense")
        fetchTransactions("Income")
        binding.chipExpenses.isChecked=true

        return binding.root
    }



    private fun setupRecyclerView(list: List<TransactionEntity> =emptyList()) {
        adapter = RvAdapter(list, clickListener ={transactionEntity -> itemClickListener(transactionEntity)  },
            doubleClickListener = {transactionEntity ->itemLongClickListener(transactionEntity)})
        binding.recycleView.adapter = adapter
    }

    private fun itemLongClickListener(transactionEntity: TransactionEntity) {
      AlertDialog.Builder(requireContext()).setTitle("Delete Record").setMessage("Are you sure you want to delete this record?")
            .setPositiveButton("Yes")
            {
                dialog,_->
                transactionViewModel.deleteTransaction(transactionEntity)
                Snackbar.make(requireView(),"${transactionEntity.type} record has been deleted successfully",Snackbar.LENGTH_LONG).setAction("Okay"){}.show()
                dialog.dismiss()
            }
            .setNegativeButton("No")
            {
                dialog,_->
                dialog.dismiss()
            }.show()
    }


    private fun itemClickListener(transactionEntity: TransactionEntity)
    {
        try
        {
            val action=HomeFragmentDirections.actionHomeFragmentToTransactionFragment(transactionEntity)
            findNavController().navigate(action)
        }
        catch (ex:Exception)
        {
            ex.printStackTrace()
        }

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
            transactionViewModel.saveRemainingAmount(requireContext())
        }

        transactionViewModel.totalExpense.observe(viewLifecycleOwner) { money ->
            binding.textExpenseMoneyValue.text = money.toString()
            transactionViewModel.saveRemainingAmount(requireContext())
        }

        transactionViewModel.remainingIncome.observe(viewLifecycleOwner) { money ->
            binding.textRemainingMoneyValue.text = money.toString()
            transactionViewModel.saveRemainingAmount(requireContext())


        }

    }

    private fun setListeners() {
        binding.chipExpenses.setOnClickListener {
            fetchTransactions("Expense")
        }
        binding.chipDeposits.setOnClickListener {
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
    override fun onResume() {
        super.onResume()
        transactionViewModel.getSaveIncome(requireContext())
    }

    private fun setFloatingButtonListener()
    {
        binding.floatingActionButton.setOnClickListener()
        {
            findNavController().navigate(R.id.action_homeFragment_to_transactionFragment)
        }
    }



}
