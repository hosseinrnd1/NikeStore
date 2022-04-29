package com.myapp.nikestore.feachers.favorites

import androidx.lifecycle.MutableLiveData
import com.myapp.nikestore.NikeViewModel
import com.myapp.nikestore.common.NikeCompletableObserver
import com.myapp.nikestore.common.NikeSingleObserver
import com.myapp.nikestore.data.Product
import com.myapp.nikestore.data.repo.ProductRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class FavoriteProductsViewModel(val productRepository: ProductRepository):NikeViewModel() {
    val favoriteProductLiveData=MutableLiveData<List<Product>>()
    init {
        productRepository.getFavoriteProduct()
            .subscribeOn(Schedulers.io())
            .subscribe(object:NikeSingleObserver<List<Product>>(compositeDisposable){
                override fun onSuccess(t: List<Product>) {
                    favoriteProductLiveData.postValue(t)
                }

            })
    }

    fun removeFromFavorites(product: Product){
        productRepository.deleteFromFavorites(product)
            .subscribeOn(Schedulers.io())
            .subscribe(object:NikeCompletableObserver(compositeDisposable){
                override fun onComplete() {
                    Timber.i("RemoveFromFavorites Completed")
                }

            })

    }
}