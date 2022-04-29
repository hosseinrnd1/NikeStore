package com.myapp.nikestore.product

import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import com.myapp.nikestore.NikeViewModel
import com.myapp.nikestore.common.EXTRA_KEY_DATA
import com.myapp.nikestore.common.NikeSingleObserver
import com.myapp.nikestore.common.asyncNetworkRequest
import com.myapp.nikestore.data.Comment
import com.myapp.nikestore.data.Product
import com.myapp.nikestore.data.repo.CartRepository
import com.myapp.nikestore.data.repo.CommentRepository
import com.myapp.nikestore.view.NikeImageView
import io.reactivex.Completable
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ProductDetailViewModel(bundle: Bundle,commentRepository: CommentRepository,val cartRepository: CartRepository): NikeViewModel() {

    val productLiveData=MutableLiveData<Product>()
    val commentsLiveData=MutableLiveData<List<Comment>>()
    init {
        productLiveData.value=bundle.getParcelable(EXTRA_KEY_DATA)
        prograssLiveData.value=true
        commentRepository.getAll(productLiveData.value!!.id)
            .asyncNetworkRequest()
            .doFinally { prograssLiveData.value=false }
            .subscribe(object :NikeSingleObserver<List<Comment>>(compositeDisposable){
                override fun onSuccess(t: List<Comment>) {
                    commentsLiveData.value=t
                }

            })
    }
    fun onAddToCartBtn():Completable=cartRepository.addToCart(productLiveData.value!!.id).ignoreElement()
}