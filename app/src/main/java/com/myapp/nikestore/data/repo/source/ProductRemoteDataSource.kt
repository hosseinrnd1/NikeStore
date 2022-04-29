package com.myapp.nikestore.data.repo.source

import com.myapp.nikestore.data.Product
import com.myapp.nikestore.services.http.ApiService
import io.reactivex.Completable
import io.reactivex.Single

class ProductRemoteDataSource(val apiService: ApiService):ProductDataSource {
    override fun getProduct(sort:Int): Single<List<Product>> {
       return apiService.getProducts(sort.toString())
    }

    override fun getFavoriteProduct(): Single<List<Product>> {
        TODO("Not yet implemented")
    }

    override fun addToFavorites(product: Product): Completable {
        TODO("Not yet implemented")
    }

    override fun deleteFromFavorites(product: Product): Completable {
        TODO("Not yet implemented")
    }
}