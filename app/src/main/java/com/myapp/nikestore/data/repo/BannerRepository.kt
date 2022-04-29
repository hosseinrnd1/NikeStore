package com.myapp.nikestore.data.repo

import com.myapp.nikestore.data.Banner
import io.reactivex.Single

interface BannerRepository {
    fun getBanner(): Single<List<Banner>>
}