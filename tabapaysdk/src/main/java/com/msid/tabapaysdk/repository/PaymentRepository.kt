package com.msid.tabapaysdk.repository

import com.msid.tabapaysdk.model.PaymentMethod
import com.msid.tabapaysdk.model.PaymentTransaction
import com.msid.tabapaysdk.util.PaymentResult

interface PaymentRepository {

    fun initiatePayment(transaction: PaymentTransaction): PaymentResult

    // Method for adding a new payment method
    fun addPaymentMethod(paymentMethod: PaymentMethod): PaymentResult

    // Method for removing an existing payment method
    fun removePaymentMethod(paymentMethodId: String): PaymentResult

    // Method for retrieving all saved payment methods
    fun getAllPaymentMethods(): List<PaymentMethod>

    // Method for retrieving transaction history for a user
    fun getTransactionHistory(userId: String): List<PaymentTransaction>

}