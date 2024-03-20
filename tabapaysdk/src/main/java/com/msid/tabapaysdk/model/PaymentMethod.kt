package com.msid.tabapaysdk.model

data class PaymentMethod(
    val id: String,
    val userId: String,  // Add userId property
    val cardNumber: String,
    val expirationDate: String,
    val cvv: String
)