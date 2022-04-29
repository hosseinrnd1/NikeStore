package com.myapp.nikestore.data.repo.order

import com.myapp.nikestore.data.CheckOut
import com.myapp.nikestore.data.SubmitOrderResponse
import com.sevenlearn.nikestore.data.OrderHistoryItem
import io.reactivex.Single

class OrderRepositoryImp(val orderRemoteDataSource: OrderDataSource) : OrderRepository {
    override fun submit(
        firstName: String,
        lastName: String,
        postalCode: String,
        phoneNumber: String,
        address: String,
        paymentMethod: String
    ): Single<SubmitOrderResponse> {
        return orderRemoteDataSource.submit(
            firstName,
            lastName,
            postalCode,
            phoneNumber,
            address,
            paymentMethod
        )
    }

    override fun checkOut(orderId: Int): Single<CheckOut> {
        return orderRemoteDataSource.checkOut(orderId)
    }

    override fun getOrders(): Single<List<OrderHistoryItem>> {
        return orderRemoteDataSource.getOrders()
    }
}