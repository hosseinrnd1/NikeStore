package com.myapp.nikestore.data.repo

import com.myapp.nikestore.data.Product
import com.myapp.nikestore.data.repo.source.ProductDataSource
import com.myapp.nikestore.data.repo.source.ProductLocalDataSource
import com.myapp.nikestore.data.repo.source.ProductRemoteDataSource
import io.reactivex.Completable
import io.reactivex.Single

class ProductRepositoryImp(val remoteDataSource: ProductDataSource,val localDataSource: ProductLocalDataSource):ProductRepository {
    override fun getProduct(sort:Int): Single<List<Product>> {
      return  localDataSource.getFavoriteProduct().flatMap { favoriteProducts->
            remoteDataSource.getProduct(sort).doOnSuccess {
                val favoriteProductsId=favoriteProducts.map {
                    it.id
                   }
                it.forEach { product->
                    if(favoriteProductsId.contains(product.id)){
                        product.isFavorite=true
                    }
                }
            }
        }

    }

    override fun getFavoriteProduct(): Single<List<Product>> =localDataSource.getFavoriteProduct()

    override fun addToFavorites(product: Product): Completable =localDataSource.addToFavorites(product)

    override fun deleteFromFavorites(product: Product): Completable =localDataSource.deleteFromFavorites(product)
}