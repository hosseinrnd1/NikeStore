package com.sevenlearn.nikestore.data

import com.myapp.nikestore.data.Product

data class CartItem(
    val cart_item_id: Int,
    var count: Int,
    val product: Product,
    var changeCountPrograssBarIsVisible: Boolean = false
)