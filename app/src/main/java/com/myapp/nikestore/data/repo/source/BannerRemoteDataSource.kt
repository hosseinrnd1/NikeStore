package com.myapp.nikestore.data.repo.source

import com.myapp.nikestore.data.Banner
import com.myapp.nikestore.services.http.ApiService
import io.reactivex.Single

class BannerRemoteDataSource(val apiService: ApiService):BannerDataSource {
    override fun getBanner(): Single<List<Banner>> = apiService.getBanner()

}