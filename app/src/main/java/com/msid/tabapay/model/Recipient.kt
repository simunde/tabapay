package com.msid.tabapay.model

data class Recipient(
    val id: Int,
    val userId: String,
    val name: String,
    val email: String,
    val profileImage: Int // Resource ID of the profile image
)