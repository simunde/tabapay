package com.msid.tabapaysdk.model



data class PaymentTransaction(
    val id: String,
    val userId: String,
    val name: String,
    val amount: Double,
    val currency: String,
    val status: PaymentStatus
)