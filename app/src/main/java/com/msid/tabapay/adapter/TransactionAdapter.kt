package com.msid.tabapay.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.msid.tabapay.R
import com.msid.tabapaysdk.model.PaymentStatus
import com.msid.tabapaysdk.model.PaymentTransaction

class TransactionAdapter(private var transactions: List<PaymentTransaction>) :
    RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {


    fun updateTransactions(newTransactions: List<PaymentTransaction>) {
        transactions = newTransactions
        notifyDataSetChanged()
    }

    inner class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val relativeTransaction: RelativeLayout = itemView.findViewById(R.id.relativeTransaction)

        private val recipientNameTextView: TextView = itemView.findViewById(R.id.recipientNameTextView)
        private val amountTextView: TextView = itemView.findViewById(R.id.transactionAmountTextView)
        private val statusTextView: TextView = itemView.findViewById(R.id.transactionStatusTextView)
        private val statusIconImageView: ImageView = itemView.findViewById(R.id.statusIconImageView)

        fun bind(transaction: PaymentTransaction) {
            recipientNameTextView.text = transaction.name
            amountTextView.text = "${transaction.amount} ${transaction.currency}"
            statusTextView.text = transaction.status.toString()
            // Set status icon drawable
            val statusIconDrawable = when (transaction.status) {
                PaymentStatus.SUCCESS -> R.drawable.success
                PaymentStatus.FAILED -> R.drawable.failed
                else -> {

                }
            }
            statusIconImageView.setImageResource(statusIconDrawable as Int)
            // Set background color based on status
            val backgroundColor = when (transaction.status) {
                PaymentStatus.SUCCESS -> ContextCompat.getColor(itemView.context, R.color.success)
                PaymentStatus.FAILED -> ContextCompat.getColor(itemView.context, R.color.failed)
                else -> {

                }
            }
            relativeTransaction.setBackgroundColor(backgroundColor as Int)


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(transactions[position])
    }

    override fun getItemCount(): Int {
        return transactions.size
    }


}