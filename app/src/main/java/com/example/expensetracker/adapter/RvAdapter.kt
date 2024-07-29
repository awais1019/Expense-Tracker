package com.example.expensetracker.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.expensetracker.databinding.RecycleViewItemBinding
import com.example.expensetracker.db.TransactionEntity
import java.text.SimpleDateFormat
import java.util.*

class RvAdapter(
    private val list: List<TransactionEntity>
) : RecyclerView.Adapter<RvAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecycleViewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val transaction = list[position]
        holder.bind(transaction, position)
    }

    inner class ViewHolder(private val binding: RecycleViewItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(transaction: TransactionEntity, position: Int) {
            val positionNumber = position + 1
            val combination="${positionNumber}- ${transaction.title}"
            binding.textViewTitle.text = combination
            binding.textViewAmount.text = transaction.amount.toString()

            val date = parseDateTime(transaction.dateTime)
            date?.let {
                binding.textViewDate.text = formatDate(it)
                binding.textViewTime.text = formatTime(it)
            }
        }

        private fun parseDateTime(dateTimeString: String): Date? {

            val originalFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
            return originalFormat.parse(dateTimeString)
        }

        private fun formatDate(date: Date): String {

            val displayDateFormat = SimpleDateFormat("d MMMM yyyy", Locale.getDefault())
            return displayDateFormat.format(date)
        }

        private fun formatTime(date: Date): String {

            val displayTimeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
            return displayTimeFormat.format(date)
        }
    }

}
