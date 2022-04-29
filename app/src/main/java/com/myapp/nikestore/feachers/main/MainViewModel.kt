package com.myapp.nikestore.feachers.main

import com.myapp.nikestore.NikeViewModel
import com.myapp.nikestore.common.NikeSingleObserver
import com.myapp.nikestore.data.TokenContainer
import com.myapp.nikestore.data.repo.CartRepository
import com.sevenlearn.nikestore.data.CartItemCount
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus

class MainViewModel(val cartRepository: CartRepository):NikeViewModel() {
    fun getCartItemsCount(){
        if(!TokenContainer.token.isNullOrEmpty()){
            cartRepository.getCartItemsCount()
                .subscribeOn(Schedulers.io())
                .subscribe(object:NikeSingleObserver<CartItemCount>(compositeDisposable){
                    override fun onSuccess(t: CartItemCount) {
                        EventBus.getDefault().postSticky(t)
                    }

                })
        }
    }

}