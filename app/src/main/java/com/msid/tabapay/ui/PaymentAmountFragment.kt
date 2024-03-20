package com.msid.tabapay.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.msid.tabapay.R
import com.msid.tabapaysdk.error.PaymentException
import com.msid.tabapaysdk.model.PaymentStatus
import com.msid.tabapaysdk.model.PaymentTransaction
import com.msid.tabapaysdk.network.MockPaymentService
import com.msid.tabapaysdk.util.PaymentResult
import java.util.UUID

class PaymentAmountFragment : Fragment() {

    private lateinit var paymentAmountEditText: EditText
    private lateinit var paymentDescriptionEditText: EditText
    private lateinit var confirmPaymentButton: Button
    private lateinit var paymentProgressBar: ProgressBar
    private lateinit var paymentStatusTextView: TextView
    private lateinit var cancelPaymentButton: Button
    private lateinit var paymentResultTextView: TextView
    private lateinit var paymentMethodRadioGroup: RadioGroup
    private lateinit var paymentService: MockPaymentService
    private var paymentAmount = 0.0
    private var userId:String?=null
    private var name:String?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_payment_amount, container, false)
        // Retrieve arguments passed from TransactionHistoryFragment
        name = arguments?.getString("name")
        userId = arguments?.getString("userId")

        initializeViews(rootView)
        setupClickListener(rootView)
//        setupPaymentProcess(rootView)
        paymentService = MockPaymentService()

        arguments?.let { bundle ->
            paymentAmount = bundle.getDouble("paymentAmount", 0.0)
            val paymentDescription = bundle.getString("paymentDescription", "")
            // Use paymentAmount and paymentDescription as needed
        }
        return rootView
    }

    private fun initializeViews(rootView: View) {
        paymentAmountEditText = rootView.findViewById(R.id.paymentAmountEditText)
        paymentDescriptionEditText = rootView.findViewById(R.id.paymentDescriptionEditText)
        confirmPaymentButton = rootView.findViewById(R.id.confirmPaymentButton)
        paymentProgressBar = rootView.findViewById(R.id.paymentProgressBar)
        paymentStatusTextView = rootView.findViewById(R.id.paymentStatusTextView)
        cancelPaymentButton = rootView.findViewById(R.id.cancelPaymentButton)
        paymentResultTextView = rootView.findViewById(R.id.paymentResultTextView)
        paymentMethodRadioGroup = rootView.findViewById(R.id.paymentMethodRadioGroup)
        cancelPaymentButton.setOnClickListener {
            cancelPayment()
            showToast("Payment cancelled by user")

        }
    }

    private fun setupClickListener(rootView: View) {
        confirmPaymentButton.setOnClickListener {
            // Use class-level variable paymentAmount instead of declaring a new one
            paymentAmount = paymentAmountEditText.text.toString().toDoubleOrNull() ?: 0.0
            val paymentDescription = paymentDescriptionEditText.text.toString()
            initiatePayment(rootView)
            if (paymentAmount != 0.0 && paymentDescription.isNotEmpty()) {
                val bundle = Bundle().apply {
                    putDouble("paymentAmount", paymentAmount)
                    putString("paymentDescription", paymentDescription)
                }
                findNavController().navigate(R.id.action_paymentAmountFragment_to_recipientListFragment)
                showToast("Money sent")

            } else {
                // Handle case where payment amount or description is not valid
            }
        }
    }

    private fun setupPaymentProcess(rootView: View) {
        confirmPaymentButton.setOnClickListener {
            initiatePayment(rootView)
        }
    }
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
    private fun initiatePayment(rootView: View) {
        val selectedPaymentMethodId = paymentMethodRadioGroup.checkedRadioButtonId
        if (selectedPaymentMethodId != -1) {
            showPaymentInProgress()
            val selectedPaymentMethod =
                rootView.findViewById<RadioButton>(selectedPaymentMethodId).text.toString()
            val transactionId = UUID.randomUUID().toString()
            val transaction = PaymentTransaction(transactionId, userId!!, name!!, paymentAmount, "USD", PaymentStatus.PENDING)
            try {
                val paymentResult = paymentService.processPayment(transaction)
                handlePaymentResult(paymentResult)
                // If payment is successful, update user balance and add transaction to history
                if (paymentResult is PaymentResult.Success) {
                    paymentService.updateBalance(transaction.userId, -transaction.amount) // Deduct payment amount from user's balance
                    paymentService.addTransactionToHistory(transaction) // Add transaction to history
                }
            } catch (ex: PaymentException) {
                showPaymentResult(ex.message ?: "Payment failed: Insufficient funds")
            }
        } else {
            showPaymentResult("Please select a payment method.")
        }
    }

    private fun handlePaymentResult(paymentResult: PaymentResult) {
        when (paymentResult) {
            is PaymentResult.Success -> showPaymentResult("Payment Successful")
            is PaymentResult.Failure -> showPaymentResult("Payment Failed: ${paymentResult.errorMessage}")
        }
    }

    private fun showPaymentInProgress() {
        paymentProgressBar.visibility = View.VISIBLE
        paymentStatusTextView.visibility = View.VISIBLE
        cancelPaymentButton.visibility = View.VISIBLE
        paymentResultTextView.visibility = View.GONE
    }

    private fun showPaymentResult(result: String) {
        paymentProgressBar.visibility = View.GONE
        paymentStatusTextView.visibility = View.GONE
        cancelPaymentButton.visibility = View.GONE
        paymentResultTextView.visibility = View.VISIBLE
        paymentResultTextView.text = result
    }

    private fun cancelPayment() {
        paymentProgressBar.visibility = View.GONE
        paymentStatusTextView.visibility = View.GONE
        paymentResultTextView.visibility = View.VISIBLE
        paymentResultTextView.text = "Payment cancelled by user"
        findNavController().popBackStack(R.id.recipientListFragment, false)

    }
}
