package com.myapp.nikestore.data

data class CheckOut(
    val payable_price: Int,
    val payment_status: String,
    val purchase_success: Boolean
)