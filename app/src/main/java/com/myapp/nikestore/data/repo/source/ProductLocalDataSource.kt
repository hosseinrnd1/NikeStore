package com.myapp.nikestore.data.repo.source

import androidx.room.*
import com.myapp.nikestore.data.Product
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface ProductLocalDataSource:ProductDataSource {
    override fun getProduct(sort:Int): Single<List<Product>> {
        TODO("Not yet implemented")
    }

    @Query("SELECT *FROM product")
    override fun getFavoriteProduct(): Single<List<Product>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override fun addToFavorites(product: Product): Completable

    @Delete
    override fun deleteFromFavorites(product: Product): Completable
}