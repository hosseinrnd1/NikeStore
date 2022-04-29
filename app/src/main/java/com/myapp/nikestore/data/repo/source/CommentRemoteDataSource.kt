package com.myapp.nikestore.data.repo.source

import com.myapp.nikestore.data.Comment
import com.myapp.nikestore.services.http.ApiService
import io.reactivex.Single

class CommentRemoteDataSource(val apiService: ApiService) :CommentDataSource {
    override fun getAll(productId: Int): Single<List<Comment>> {
        return apiService.getComments(productId)
    }

    override fun insert(comment: Comment): Single<Comment> {
        TODO("Not yet implemented")
    }

}