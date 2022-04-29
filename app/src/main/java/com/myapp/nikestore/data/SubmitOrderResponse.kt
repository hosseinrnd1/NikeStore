package com.myapp.nikestore.data

data class SubmitOrderResponse(
    val bank_gateway_url: String,
    val order_id: Int
)