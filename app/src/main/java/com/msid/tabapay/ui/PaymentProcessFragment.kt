package com.msid.tabapay.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import com.msid.tabapay.R
import com.msid.tabapaysdk.error.PaymentException
import com.msid.tabapaysdk.model.PaymentStatus
import com.msid.tabapaysdk.model.PaymentTransaction
import com.msid.tabapaysdk.network.MockPaymentService
import com.msid.tabapaysdk.util.PaymentResult


class PaymentProcessFragment : Fragment() {

    private lateinit var paymentProgressBar: ProgressBar
    private lateinit var paymentStatusTextView: TextView
    private lateinit var cancelPaymentButton: Button
    private lateinit var paymentResultTextView: TextView
    private lateinit var paymentMethodRadioGroup: RadioGroup
    private lateinit var paymentService: MockPaymentService
    private lateinit var rootView: View // Declare rootView as a class-level property
    private  var  paymentAmount=0.0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_payment_process, container, false)
        initializeViews(rootView)
        setupPaymentProcess()
        paymentService = MockPaymentService()


        // Retrieve arguments
        arguments?.let { bundle ->
            paymentAmount = bundle.getDouble("paymentAmount", 0.0) // 0.0 is a default value
            val paymentDescription = bundle.getString("paymentDescription", "") // Empty string is a default value

            // Use paymentAmount and paymentDescription as needed
        }
        return rootView
    }

    private fun initializeViews(rootView: View) {
        paymentProgressBar = rootView.findViewById(R.id.paymentProgressBar)
        paymentStatusTextView = rootView.findViewById(R.id.paymentStatusTextView)
        cancelPaymentButton = rootView.findViewById(R.id.cancelPaymentButton)
        paymentResultTextView = rootView.findViewById(R.id.paymentResultTextView)
        paymentMethodRadioGroup = rootView.findViewById(R.id.paymentMethodRadioGroup)
        cancelPaymentButton.setOnClickListener {
            // Call a method to handle cancellation logic
            cancelPayment()        }
    }

    private fun cancelPayment() {
        // Hide progress bar and related views
        paymentProgressBar.visibility = View.GONE
        paymentStatusTextView.visibility = View.GONE
        paymentResultTextView.visibility = View.VISIBLE
        paymentResultTextView.text = "Payment cancelled by user"
    }

    private fun setupPaymentProcess() {
        rootView.findViewById<Button>(R.id.confirmPaymentButton).setOnClickListener {
            initiatePayment()
        }
    }

    private fun initiatePayment() {
        val selectedPaymentMethodId = paymentMethodRadioGroup.checkedRadioButtonId
        if (selectedPaymentMethodId != -1) {
            showPaymentInProgress()
            val selectedPaymentMethod =
                rootView.findViewById<RadioButton>(selectedPaymentMethodId).text.toString()
            val transaction = PaymentTransaction("3", "user_id_1", "John Doe",paymentAmount, "USD", PaymentStatus.PENDING)
            try {
                val paymentResult = paymentService.processPayment(transaction)
                handlePaymentResult(paymentResult)
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
}
