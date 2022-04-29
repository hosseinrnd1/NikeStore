package com.myapp.nikestore.services.http

import com.google.gson.JsonObject
import com.myapp.nikestore.data.*
import com.sevenlearn.nikestore.data.*
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @GET("product/list")
    fun getProducts(@Query("sort") sort:String):Single<List<Product>>

    @GET("banner/slider")
    fun getBanner():Single<List<Banner>>

    @GET("comment/list")
    fun getComments(@Query("product_id") productId:Int):Single<List<Comment>>

    @POST("cart/add")
    fun addToCart(@Body jsonObject: JsonObject):Single<AddToCartResponse>

    @GET("cart/list")
    fun getCart():Single<CartResponse>

    @POST("cart/remove")
    fun removeItemFromCart(@Body jsonObject: JsonObject):Single<MessageResponse>// cart_item_id

    @GET("cart/count")
    fun getCartItemsCount():Single<CartItemCount>

    @POST("cart/changeCount") //    cart_item_id  //    count
    fun changeCount(@Body jsonObject: JsonObject):Single<AddToCartResponse>

    @POST("auth/token")
    fun loginUser(@Body jsonObject: JsonObject):Single<TokenResponse>

    @POST("user/register")
    fun signUpUser(@Body jsonObject: JsonObject):Single<MessageResponse>

    @POST("auth/token")
    fun refreshToken(@Body jsonObject: JsonObject):Call<TokenResponse>

    @POST("order/submit")
    fun submitOrder(@Body jsonObject: JsonObject):Single<SubmitOrderResponse>

    @GET("order/checkout")
    fun checkOut(@Query("order_id") orderId:Int):Single<CheckOut>

    @GET("order/list")
    fun getOrders():Single<List<OrderHistoryItem>>



}

fun createApiServiceInstance():ApiService{
    val okHttpClient=OkHttpClient.Builder()
        .addInterceptor {
            val oldRequest=it.request()
            val newRequestBuilder=oldRequest.newBuilder()
            if(TokenContainer.token!=null)
                newRequestBuilder.addHeader("Authorization","Bearer ${TokenContainer.token}")
            newRequestBuilder.addHeader("Accept","application/json")
            newRequestBuilder.method(oldRequest.method,oldRequest.body)
            return@addInterceptor it.proceed(newRequestBuilder.build())
        }
        .addInterceptor(HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)})
        .build()

    val retrofit=Retrofit.Builder()
        .baseUrl("http://expertdevelopers.ir/api/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(okHttpClient)
        .build()

    return retrofit.create(ApiService::class.java)
}