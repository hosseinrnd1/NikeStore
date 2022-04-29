package com.myapp.nikestore.feachers.list

import androidx.lifecycle.MutableLiveData
import com.myapp.nikestore.NikeViewModel
import com.myapp.nikestore.R
import com.myapp.nikestore.common.NikeCompletableObserver
import com.myapp.nikestore.common.NikeSingleObserver
import com.myapp.nikestore.common.asyncNetworkRequest
import com.myapp.nikestore.data.*
import com.myapp.nikestore.data.repo.ProductRepository

class ProductListViewModel(var sort :Int,val productRepository: ProductRepository):NikeViewModel() {
    val productLiveData=MutableLiveData<List<Product>>()
    val selectedSortTitleLiveData=MutableLiveData<Int>()
    val sortTitles= arrayOf(R.string.sortLatest, R.string.sortPopular, R.string.sortPriceHighToLow, R.string.sortPriceLowToHigh)
    init {
        getProducts()
        selectedSortTitleLiveData.value=sortTitles[sort]

    }

    private fun getProducts() {
        prograssLiveData.value=true
        productRepository.getProduct(sort)
            .asyncNetworkRequest()
            .doFinally { prograssLiveData.value=false }
            .subscribe(object :NikeSingleObserver<List<Product>>(compositeDisposable){
                override fun onSuccess(t: List<Product>) {
                    productLiveData.value=t
                }

            })
    }

    fun onSelectedSortChangeByUser(sort:Int){
        this.sort=sort
        getProducts()
        this.selectedSortTitleLiveData.value=sortTitles[sort]
    }

    fun addProductToFavorite(product: Product){
        if(product.isFavorite){
            productRepository.deleteFromFavorites(product)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .subscribe(object: NikeCompletableObserver(compositeDisposable){
                    override fun onComplete() {
                        product.isFavorite=false
                    }

                })
        }
        else{
            productRepository.addToFavorites(product)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .subscribe(object: NikeCompletableObserver(compositeDisposable){
                    override fun onComplete() {
                        product.isFavorite=true
                    }

                })
        }
    }


}