package com.myapp.nikestore.data.repo.source

import com.myapp.nikestore.data.Product
import io.reactivex.Completable
import io.reactivex.Single

interface ProductDataSource {
    fun getProduct(sort:Int): Single<List<Product>>

    fun getFavoriteProduct(): Single<List<Product>>

    fun addToFavorites(product: Product): Completable

    fun deleteFromFavorites(product: Product): Completable
}