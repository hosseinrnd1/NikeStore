package com.myapp.nikestore.feachers.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.work.impl.Schedulers
import com.myapp.nikestore.NikeViewModel
import com.myapp.nikestore.common.NikeCompletableObserver
import com.myapp.nikestore.common.NikeSingleObserver
import com.myapp.nikestore.common.asyncNetworkRequest
import com.myapp.nikestore.data.*
import com.myapp.nikestore.data.repo.BannerRepository
import com.myapp.nikestore.data.repo.ProductRepository
import io.reactivex.Scheduler
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers.io
import kotlinx.coroutines.flow.combineTransform
import timber.log.Timber

class HomeViewModel(private val productRepository: ProductRepository, bannerRepository: BannerRepository):NikeViewModel() {
     val latest_productLiveData = MutableLiveData<List<Product>>()
     val bestSelling_productLiveData = MutableLiveData<List<Product>>()
     val bannerLiveData = MutableLiveData<List<Banner>>()

//    val prograssLiveData=MutableLiveData<Boolean>()

    init {
        prograssLiveData.value=true
        productRepository.getProduct(SORT_LATEST)
            .asyncNetworkRequest()
            .doFinally { prograssLiveData.value=false }
            .subscribe(object :NikeSingleObserver<List<Product>>(compositeDisposable){
                override fun onSuccess(t: List<Product>) {
                    latest_productLiveData.value=t
                }
            })
        productRepository.getProduct(SORT_POPULAR)
            .asyncNetworkRequest()
            .subscribe(object :NikeSingleObserver<List<Product>>(compositeDisposable){
                override fun onSuccess(t: List<Product>) {
                    bestSelling_productLiveData.value=t
                }

            })

        bannerRepository.getBanner()
            .asyncNetworkRequest()
            .subscribe(object:NikeSingleObserver<List<Banner>>(compositeDisposable){
                override fun onSuccess(t: List<Banner>) {
                    bannerLiveData.value=t
                }

            })


    }

    fun addProductToFavorite(product: Product){
        if(product.isFavorite){
            productRepository.deleteFromFavorites(product)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .subscribe(object:NikeCompletableObserver(compositeDisposable){
                    override fun onComplete() {
                        product.isFavorite=false
                    }

                })
        }
        else{
            productRepository.addToFavorites(product)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .subscribe(object:NikeCompletableObserver(compositeDisposable){
                    override fun onComplete() {
                        product.isFavorite=true
                    }

                })
        }
    }
}