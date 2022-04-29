package com.myapp.nikestore.data.repo

import com.myapp.nikestore.data.Banner
import com.myapp.nikestore.data.repo.source.BannerDataSource
import com.myapp.nikestore.data.repo.source.BannerRemoteDataSource
import io.reactivex.Single

class BannerRepositoryImp(val remoteDataSource: BannerDataSource) : BannerRepository {
    override fun getBanner(): Single<List<Banner>> =remoteDataSource.getBanner()

}