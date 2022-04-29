package com.myapp.nikestore.feachers.shipping

import com.google.gson.JsonObject
import com.myapp.nikestore.NikeViewModel
import com.myapp.nikestore.data.CheckOut
import com.myapp.nikestore.data.SubmitOrderResponse
import com.myapp.nikestore.data.repo.order.OrderRepository
import io.reactivex.Single
const val PAYMENT_METHOD_ONLINE="online"
const val PAYMENT_METHOD_COD="cash_on_delivery"
class ShippingViewModel(private val orderRepository: OrderRepository):NikeViewModel() {
     fun submit(
        firstName: String,
        lastName: String,
        postalCode: String,
        phoneNumber: String,
        address: String,
        paymentMethod: String
    ): Single<SubmitOrderResponse> {
        return orderRepository.submit(firstName,lastName,postalCode,phoneNumber,address,paymentMethod)
    }


}