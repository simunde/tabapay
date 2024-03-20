package com.msid.tabapay.model


//
data class Transaction(
    val id: String,
    val amount: Double,
    val name: String,
    val currency: String,
    val status: String
)


