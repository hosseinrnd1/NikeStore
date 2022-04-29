package com.myapp.nikestore.data.repo

import com.myapp.nikestore.data.repo.source.CartDataSource
import com.myapp.nikestore.data.repo.source.CartRemoteDataSource
import com.sevenlearn.nikestore.data.AddToCartResponse
import com.sevenlearn.nikestore.data.CartItemCount
import com.sevenlearn.nikestore.data.CartResponse
import com.sevenlearn.nikestore.data.MessageResponse
import io.reactivex.Single

class CartRepositoryImp(val cartRemoteDataSource: CartDataSource):CartRepository {
    override fun addToCart(productId: Int): Single<AddToCartResponse> = cartRemoteDataSource.addToCart(productId)

    override fun get(): Single<CartResponse> {
        return cartRemoteDataSource.get()
    }

    override fun remove(cartItemId: Int): Single<MessageResponse> {
        return cartRemoteDataSource.remove(cartItemId)
    }

    override fun changeCount(cartItemId: Int, count: Int): Single<AddToCartResponse> {
        return cartRemoteDataSource.changeCount(cartItemId,count)
    }

    override fun getCartItemsCount(): Single<CartItemCount> {
        return cartRemoteDataSource.getCartItemsCount()
    }
}