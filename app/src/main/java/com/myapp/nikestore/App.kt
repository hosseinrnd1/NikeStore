package com.myapp.nikestore

import android.app.Application
import android.content.SharedPreferences
import android.os.Bundle
import androidx.room.Room
import com.facebook.drawee.backends.pipeline.Fresco
import com.myapp.nikestore.data.db.AppDataBase
import com.myapp.nikestore.data.repo.*
import com.myapp.nikestore.data.repo.order.OrderRemoteDataSource
import com.myapp.nikestore.data.repo.order.OrderRepository
import com.myapp.nikestore.data.repo.order.OrderRepositoryImp
import com.myapp.nikestore.data.repo.source.*
import com.myapp.nikestore.feachers.auth.AuthViewModel
import com.myapp.nikestore.feachers.cart.CartViewModel
import com.myapp.nikestore.feachers.checkout.CheckOutViewModel
import com.myapp.nikestore.feachers.home.HomeViewModel
import com.myapp.nikestore.feachers.common.ProductListAdapter
import com.myapp.nikestore.feachers.favorites.FavoriteProductsViewModel
import com.myapp.nikestore.feachers.list.ProductListViewModel
import com.myapp.nikestore.feachers.main.MainViewModel
import com.myapp.nikestore.feachers.order.OrderHistoryViewModel
import com.myapp.nikestore.feachers.profile.ProfileViewModel
import com.myapp.nikestore.feachers.shipping.ShippingViewModel
import com.myapp.nikestore.product.ProductDetailViewModel
import com.myapp.nikestore.product.comment.CommentListViewModel
import com.myapp.nikestore.services.FrescoImageLoadingService
import com.myapp.nikestore.services.ImageLoadingService
import com.myapp.nikestore.services.http.ApiService
import com.myapp.nikestore.services.http.createApiServiceInstance
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import timber.log.Timber

class App :Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        Fresco.initialize(this)

        val modules= module {
            single { Room.databaseBuilder(this@App,AppDataBase::class.java,"db_app").build() }
            single<OrderRepository> { OrderRepositoryImp(OrderRemoteDataSource(get())) }
            single { UserLocalDataSource(get()) }
            single<SharedPreferences> {this@App.getSharedPreferences("app_setting", MODE_PRIVATE)}
            single<ApiService> { createApiServiceInstance() }
            single<ImageLoadingService> { FrescoImageLoadingService() }
            factory<ProductRepository> {ProductRepositoryImp(ProductRemoteDataSource(get()),
                get<AppDataBase>().getProductDao()
            )  }
            factory<BannerRepository> { BannerRepositoryImp(BannerRemoteDataSource(get()))}
            factory { (viewtype:Int)->ProductListAdapter(viewtype,get()) }
            factory<CommentRepository> { CommentRepositoryImp(CommentRemoteDataSource(get())) }
            factory<CartRepository> { CartRepositoryImp(CartRemoteDataSource(get())) }
            single <UserRepository> {UserRepositoryImp(UserLocalDataSource(get()),UserRemoteDataSource(get())) }
            viewModel { HomeViewModel(get(),get()) }
            viewModel { (bundle:Bundle)->ProductDetailViewModel(bundle,get(),get()) }
            viewModel { (productId:Int)->CommentListViewModel(productId,get()) }
            viewModel {(sort:Int)->ProductListViewModel(sort,get())}
            viewModel { AuthViewModel(get()) }
            viewModel {CartViewModel(get())}
            viewModel {MainViewModel(get())}
            viewModel {ShippingViewModel(get())}
            viewModel {(orderID:Int)->CheckOutViewModel(orderID,get())}
            viewModel {ProfileViewModel(get())}
            viewModel {FavoriteProductsViewModel(get())}
            viewModel {OrderHistoryViewModel(get())}
        }

        startKoin {
            androidContext(this@App)
            modules(modules)
        }

        val userRepository:UserRepository = get()
        userRepository.loadToken()
    }
}