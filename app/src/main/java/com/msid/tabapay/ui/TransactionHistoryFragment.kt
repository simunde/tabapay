package com.msid.tabapay.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.msid.tabapay.R
import com.msid.tabapay.adapter.TransactionAdapter
import com.msid.tabapaysdk.network.MockPaymentService


class TransactionHistoryFragment : Fragment() {


    private lateinit var recyclerView: RecyclerView
    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var transfersInitialsTextView: TextView
    private lateinit var transfersNameTextView: TextView
    private lateinit var transferTextView: TextView
    private lateinit var sendMoneyButton: Button
    private val paymentService = MockPaymentService()
    private var userId:String?=null
    private var name:String?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_transaction_history, container, false)
        setupRecyclerView(rootView)

        // Retrieve arguments
        arguments?.let { bundle ->
            name = bundle.getString("name", null)
            userId = bundle.getString("userId", null)


        }
        loadTransactionsForUser(userId!!)
        return rootView
    }

    private fun setupRecyclerView(rootView: View) {
        transfersInitialsTextView = rootView.findViewById(R.id.transfersInitialsTextView)
        transfersNameTextView = rootView.findViewById(R.id.transfersNameTextView)
        transferTextView = rootView.findViewById(R.id.transferTextView)
        recyclerView = rootView.findViewById(R.id.transactionRecyclerView)
        sendMoneyButton = rootView.findViewById(R.id.sendMoneyButton)
        sendMoneyButton.setOnClickListener {
            val bundle = Bundle().apply {
                putString("name", name) // Pass the name data
                putString("userId", userId) // Pass the userId data
            }
            findNavController().navigate(R.id.action_transactionHistoryFragment_to_paymentAmountFragment, bundle)

        }
        transactionAdapter = TransactionAdapter(emptyList()) // Pass empty list initially
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = transactionAdapter
        }
    }

    private fun loadTransactionsForUser(userId: String) {
        if (userId != null && name!=null) {
            transfersNameTextView.text=name
            val names = name!!.split(" ")
            val initials = names.mapNotNull { it.firstOrNull()?.toUpperCase() }.take(2).joinToString("")
            transfersInitialsTextView.text = initials

            val transactions = paymentService.getTransactionHistory(userId)
            transactionAdapter.updateTransactions(transactions)
        }
    }


}