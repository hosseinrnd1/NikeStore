package com.myapp.nikestore.data.repo.source

import com.myapp.nikestore.data.Banner
import io.reactivex.Single

interface BannerDataSource {
    fun getBanner(): Single<List<Banner>>
}