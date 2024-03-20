package com.msid.tabapay.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.msid.tabapay.R
import com.msid.tabapay.adapter.TransactionAdapter
import com.msid.tabapaysdk.network.MockPaymentService


class AllTransactionsFragment : Fragment() {


    private lateinit var recyclerView: RecyclerView
    private lateinit var transactionAdapter: TransactionAdapter
    private val paymentService = MockPaymentService()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_all_transactions, container, false)
        setupRecyclerView(rootView)
        loadAllTransactions()
        return rootView
    }
    private fun setupRecyclerView(rootView: View) {
        recyclerView = rootView.findViewById(R.id.allTransactionRecyclerView)
        transactionAdapter = TransactionAdapter(emptyList())
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = transactionAdapter
        }
    }

    private fun loadAllTransactions() {
        // Assuming your MockPaymentService has a method to get all transactions
        val transactions = paymentService.getAllTransactions()
        Log.d("AllTransactionsFragment", "Loading transactions: ${transactions.size}")

        transactionAdapter.updateTransactions(transactions)
    }

}