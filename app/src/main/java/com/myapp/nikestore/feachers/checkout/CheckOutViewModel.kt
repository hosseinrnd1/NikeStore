package com.myapp.nikestore.feachers.checkout

import androidx.lifecycle.MutableLiveData
import com.myapp.nikestore.NikeViewModel
import com.myapp.nikestore.common.NikeSingleObserver
import com.myapp.nikestore.common.asyncNetworkRequest
import com.myapp.nikestore.data.CheckOut
import com.myapp.nikestore.data.repo.order.OrderRepository

class CheckOutViewModel(orderId:Int,orderRepository: OrderRepository):NikeViewModel() {
    val checkOutLivedata=MutableLiveData<CheckOut>()
    init {
        orderRepository.checkOut(orderId)
            .asyncNetworkRequest()
            .subscribe(object:NikeSingleObserver<CheckOut>(compositeDisposable){
                override fun onSuccess(t: CheckOut) {
                    checkOutLivedata.value=t
                }

            })
    }
}