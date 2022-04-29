package com.myapp.nikestore.feachers.order

import androidx.lifecycle.MutableLiveData
import com.myapp.nikestore.NikeViewModel
import com.myapp.nikestore.common.NikeSingleObserver
import com.myapp.nikestore.common.asyncNetworkRequest
import com.myapp.nikestore.data.repo.order.OrderRepository
import com.sevenlearn.nikestore.data.OrderHistoryItem

class OrderHistoryViewModel(orderRepository: OrderRepository):NikeViewModel() {
    val orders=MutableLiveData<List<OrderHistoryItem>>()
    init {
        prograssLiveData.value=true
        orderRepository.getOrders()
            .asyncNetworkRequest()
            .doFinally {
                prograssLiveData.value=false
            }
            .subscribe(object:NikeSingleObserver<List<OrderHistoryItem>>(compositeDisposable){
                override fun onSuccess(t: List<OrderHistoryItem>) {
                    orders.value=t
                }

            })
    }
}