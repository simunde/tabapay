package com.msid.tabapaysdk.util

sealed class PaymentResult {

    data class Success(val transactionId: String) : PaymentResult()
    data class Failure(val errorMessage: String) : PaymentResult()
}