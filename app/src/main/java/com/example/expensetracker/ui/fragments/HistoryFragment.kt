package com.example.expensetracker.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.expensetracker.R
import com.example.expensetracker.RepositoryClass
import com.example.expensetracker.adapter.RvAdapter
import com.example.expensetracker.databinding.FragmentHistoryBinding
import com.example.expensetracker.db.DatabaseClass
import com.example.expensetracker.db.TransactionEntity
import com.example.expensetracker.viewModels.TransactionViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.text.DateFormatSymbols

@AndroidEntryPoint
class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var adapter: RvAdapter
    private val transactionViewModel: TransactionViewModel by viewModels()
    private var list= emptyArray<String>()
    private lateinit var month:String
    private lateinit var year:String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding=FragmentHistoryBinding.inflate(inflater,container,false)
        transactionViewModel.getStoredMonthAndYear(requireContext())
        observeMonthYear()
        deleteRecord()
        return binding.root
    }



    private fun observeMonthYear()
    {

        transactionViewModel.saveYearMonth.observe(viewLifecycleOwner)
        {
           if(!it.isNullOrEmpty())
           {
               this.list=it.toTypedArray()
               setAdapterForDropDown()
               binding.autoCompleteTextView.setText(list[0], false)
               setVisibility()
           }
        }
    }
    private fun setAdapterForDropDown()
    {
        val adapter = ArrayAdapter(requireContext(), R.layout.drop_down_text, list)
        binding.autoCompleteTextView.setAdapter(adapter)
        setupDropdownListener()
    }

    private fun setVisibility()
    {
        if(list.isNotEmpty())
        {
            binding.textInputLayout.visibility=View.VISIBLE
            binding.cardView.visibility=View.VISIBLE
            binding.chipGroup.visibility=View.VISIBLE
            binding.recycleView.visibility=View.VISIBLE
            binding.deleteRecord.visibility=View.VISIBLE
            separateMonthYear()
            setValues()
            setRecycleView()
            fetchTransactions("Income")
            fetchTransactions("Expense")
            binding.textViewNoRecords.visibility=View.GONE
        }
        else
        {
            binding.textInputLayout.visibility=View.GONE
            binding.cardView.visibility=View.GONE
            binding.chipGroup.visibility=View.GONE
            binding.recycleView.visibility=View.GONE
            binding.textViewNoRecords.visibility=View.VISIBLE
            binding.deleteRecord.visibility=View.GONE

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
            month,
            year,
            requireContext(),
            type
        )
    }

    private fun getSelectValueFromDropDown(): String? {

        val selectedValue = binding.autoCompleteTextView.text.toString()
        return selectedValue.ifEmpty { null }
    }

    private fun separateMonthYear()
    {
        val selectedValue = getSelectValueFromDropDown()
        if (selectedValue != null && selectedValue.length >= 6) {

            this.month = selectedValue.substring(0, 2)
            this.year = selectedValue.substring(selectedValue.length - 4)
            setListeners()
        }
    }

    private fun setupRecyclerView(list: List<TransactionEntity> =emptyList()) {
        adapter = RvAdapter(list)
        binding.recycleView.adapter = adapter
    }

    private fun setValues() {
        transactionViewModel.totalIncome.observe(viewLifecycleOwner) { money ->
            val income = "Total Income: $money"
            binding.labelDeposits.text = income
            setProgressIndicator()
        }

        transactionViewModel.totalExpense.observe(viewLifecycleOwner) { money ->
            val expense = "Total Expense: $money"
            binding.labelExpenses.text = expense
            setProgressIndicator()
        }

        transactionViewModel.remainingIncome.observe(viewLifecycleOwner) { money ->
            val saving = "Total Saving: $money"
            binding.labelRemainingAmount.text = saving
            setProgressIndicator()
        }
    }


    private fun setProgressIndicator() {
        val totalIncome = transactionViewModel.totalIncome.value ?: 0.0
        val totalExpense = transactionViewModel.totalExpense.value ?: 0.0
        if (totalIncome > 0) {
            val progress = ((totalExpense / totalIncome) * 100).toInt()
            binding.circularProgressIndicator.progress = progress
        }
    }

    private fun setRecycleView() {
        transactionViewModel.transaction.observe(viewLifecycleOwner) { transactions ->
            setupRecyclerView(transactions)
        }
        transactionViewModel.transaction2.observe(viewLifecycleOwner) { transactions ->
            setupRecyclerView(transactions)

        }
    }

    private fun deleteWholeMonthRecord() {
        val monthNumber = getMonthName(month)
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Delete Records")
        builder.setMessage("Are you sure you want to delete the history for $monthNumber $year?")

        builder.setPositiveButton("Yes") { dialog, _ ->
            transactionViewModel.deleteWholeMonthRecord(month, year, requireContext())
            Snackbar.make(requireView(), "This month's history has been deleted successfully", Snackbar.LENGTH_LONG)
                .setAction("Okay") {
                }.show()
            dialog.dismiss()
        }

        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }
    private fun setupDropdownListener() {
        binding.autoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            val selectedValue = list[position]
            binding.autoCompleteTextView.setText(selectedValue, false)
            separateMonthYear()
            fetchTransactions("Income")
            fetchTransactions("Expense")
        }
    }


    private fun getMonthName(monthNumber: String): String {
        val monthIndex = monthNumber.toInt() - 1
        return DateFormatSymbols().months[monthIndex]
    }

    private fun deleteRecord()
    {
        binding.deleteRecord.setOnClickListener {
            deleteWholeMonthRecord()
        }
    }




}