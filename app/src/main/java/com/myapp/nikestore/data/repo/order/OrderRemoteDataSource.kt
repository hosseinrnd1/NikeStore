package com.myapp.nikestore.data.repo.order

import com.google.gson.JsonObject
import com.myapp.nikestore.data.CheckOut
import com.myapp.nikestore.data.SubmitOrderResponse
import com.myapp.nikestore.services.http.ApiService
import com.sevenlearn.nikestore.data.OrderHistoryItem
import io.reactivex.Single

class OrderRemoteDataSource(val apiService: ApiService):OrderDataSource {
    override fun submit(
        firstName: String,
        lastName: String,
        postalCode: String,
        phoneNumber: String,
        address: String,
        paymentMethod: String
    ): Single<SubmitOrderResponse> {
        return apiService.submitOrder(JsonObject().apply {
            addProperty("first_name",firstName)
            addProperty("last_name",lastName)
            addProperty("postal_code",postalCode)
            addProperty("mobile",phoneNumber)
            addProperty("address",address)
            addProperty("payment_method",paymentMethod)
        })
    }

    override fun checkOut(orderId: Int): Single<CheckOut> {
        return  apiService.checkOut(orderId)
    }

    override fun getOrders(): Single<List<OrderHistoryItem>> {
        return apiService.getOrders()
    }
}