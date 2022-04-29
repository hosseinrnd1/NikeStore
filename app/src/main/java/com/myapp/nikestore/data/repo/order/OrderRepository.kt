package com.myapp.nikestore.data.repo.order

import com.myapp.nikestore.data.CheckOut
import com.myapp.nikestore.data.SubmitOrderResponse
import com.sevenlearn.nikestore.data.OrderHistoryItem
import io.reactivex.Single

interface OrderRepository {
    fun submit(firstName:String,lastName:String,postalCode:String,phoneNumber:String,address:String,paymentMethod:String)
    : Single<SubmitOrderResponse>

    fun checkOut(orderId:Int):Single<CheckOut>

    fun getOrders():Single<List<OrderHistoryItem>>

}