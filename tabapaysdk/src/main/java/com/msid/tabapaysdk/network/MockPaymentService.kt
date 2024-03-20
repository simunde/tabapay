package com.msid.tabapaysdk.network

import android.os.Build
import androidx.annotation.RequiresApi
import com.msid.tabapaysdk.error.PaymentException
import com.msid.tabapaysdk.model.PaymentMethod
import com.msid.tabapaysdk.model.PaymentStatus
import com.msid.tabapaysdk.model.PaymentTransaction
import com.msid.tabapaysdk.util.PaymentResult
import java.util.UUID

class MockPaymentService : PaymentService {

    // Simulated user balance
    private var userBalances = mapOf(
        "user_id_1" to 1000.0, // Example user with 1000 USD balance
        "user_id_2" to 2000.0, // Example user with 1000 USD balance
        "user_id_3" to 1400.0, // Example user with 1000 USD balance
        "user_id_4" to 1500.0, // Example user with 1000 USD balance
        "user_id_5" to 1700.0, // Example user with 1000 USD balance
        "user_id_6" to 1900.0, // Example user with 1000 USD balance
        "user_id_7" to 3000.0, // Example user with 1000 USD balance
        // Add more user balances as needed
    )

    // Simulated payment methods
    private val paymentMethods = mutableListOf<PaymentMethod>(
        PaymentMethod("1", "user_id_1", "1234 5678 9012 3456", "12/24", "123"),
        PaymentMethod("2", "user_id_2", "1234 5678 9012 3456", "12/24", "123"),
        PaymentMethod("3", "user_id_3", "1234 5678 9012 3456", "12/24", "123"),
        PaymentMethod("4", "user_id_4", "1234 5678 9012 3456", "12/24", "123"),
        PaymentMethod("5", "user_id_5", "1234 5678 9012 3456", "12/24", "123"),
        PaymentMethod("6", "user_id_6", "1234 5678 9012 3456", "12/24", "123"),
        PaymentMethod("7", "user_id_7", "9876 5432 1098 7654", "10/26", "456")
    )

    // Simulated transaction history
    private val transactionHistory = mutableListOf<PaymentTransaction>(
        PaymentTransaction("1", "user_id_1", "John Doe",100.0, "USD", PaymentStatus.SUCCESS),
        PaymentTransaction("2", "user_id_2", "Alice Smith",200.0, "USD", PaymentStatus.SUCCESS),
        PaymentTransaction("3", "user_id_3", "Bob Johnson",200.0, "USD", PaymentStatus.FAILED),
        PaymentTransaction("4", "user_id_4", "Emily Johnson",200.0, "USD", PaymentStatus.SUCCESS),
        PaymentTransaction("5", "user_id_5", "Michael Brown",200.0, "USD", PaymentStatus.SUCCESS),
        PaymentTransaction("6", "user_id_6", "Sarah Wilson",200.0, "USD", PaymentStatus.SUCCESS),
        PaymentTransaction("7", "user_id_7", "David Taylor",150.0, "USD", PaymentStatus.FAILED),
        PaymentTransaction("8", "user_id_6", "Sarah Wilson",300.0, "USD", PaymentStatus.FAILED),
        PaymentTransaction("9", "user_id_6", "Sarah Wilson",150.0, "USD", PaymentStatus.FAILED),
        PaymentTransaction("10", "user_id_2", "Alice Smith",250.0, "USD", PaymentStatus.FAILED)
    )

    override fun processPayment(transaction: PaymentTransaction): PaymentResult {
        // Simulate processing payment
        val userId = transaction.userId
        val amount = transaction.amount

        // Check if user has sufficient funds
        if (isInsufficientFunds(userId, amount)) {
            throw PaymentException("Payment failed: Insufficient funds")
        }

        // Process the payment (Placeholder: No actual processing)
        return PaymentResult.Success(transaction.id)
    }

    fun updateBalance(userId: String, amount: Double) {
        val currentUserBalance = userBalances.toMutableMap()
        val currentBalance = currentUserBalance[userId] ?: return
        currentUserBalance[userId] = currentBalance - amount
        userBalances = currentUserBalance // Assign the mutable map directly
    }

    fun addTransactionToHistory(transaction: PaymentTransaction) {
        val transactionId = UUID.randomUUID().toString() // Generate a unique ID
        val uniqueTransaction = transaction.copy(id = transactionId) // Assign the unique ID to the transaction
        transactionHistory.add(uniqueTransaction) // Add the transaction to the history
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun addPaymentMethod(paymentMethod: PaymentMethod): PaymentResult {
        // Encrypt and save the payment method securely
        val encryptedCardNumber = SecurityManager.encryptData(paymentMethod.cardNumber)
        val encryptedExpirationDate = SecurityManager.encryptData(paymentMethod.expirationDate)
        val encryptedCvv = SecurityManager.encryptData(paymentMethod.cvv)
        val encryptedPaymentMethod = paymentMethod.copy(
            cardNumber = encryptedCardNumber,
            expirationDate = encryptedExpirationDate,
            cvv = encryptedCvv
        )

        // Simulate adding a payment method
        // For demonstration purposes, return success
        if (isPaymentMethodExists(encryptedPaymentMethod)) {
            throw PaymentException("Failed to add payment method: Payment method already exists")
        }
        paymentMethods.add(encryptedPaymentMethod)
        return PaymentResult.Success("mock_payment_method_added")
    }

    override fun removePaymentMethod(paymentMethodId: String): PaymentResult {
        // Simulate removing a payment method
        val paymentMethod = paymentMethods.find { it.id == paymentMethodId }
        if (paymentMethod == null) {
            throw PaymentException("Failed to remove payment method: Payment method not found")
        }
        paymentMethods.remove(paymentMethod)
        return PaymentResult.Success("mock_payment_method_removed")
    }

    override fun getAllPaymentMethods(): List<PaymentMethod> {
        // Return all payment methods
        return paymentMethods.toList()
    }

    override fun getTransactionHistory(userId: String): List<PaymentTransaction> {
        // Return transaction history for the specified user
        return transactionHistory.filter { it.userId == userId }
    }

    // Method to return all transactions
    fun getAllTransactions(): List<PaymentTransaction> {
        return transactionHistory.toList() // Return a copy of the entire transaction history
    }

    // Helper method to check if the user has sufficient funds for the payment
    private fun isInsufficientFunds(userId: String, amount: Double): Boolean {
        val userBalance = userBalances[userId] ?: return true // User not found, assume insufficient funds
        return amount > userBalance
    }

    // Helper method to check if a payment method already exists
    private fun isPaymentMethodExists(paymentMethod: PaymentMethod): Boolean {
        return paymentMethods.any { it.id == paymentMethod.id }
    }
}
