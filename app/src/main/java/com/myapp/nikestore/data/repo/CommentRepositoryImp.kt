package com.myapp.nikestore.data.repo

import com.myapp.nikestore.data.Comment
import com.myapp.nikestore.data.repo.source.CommentDataSource
import com.myapp.nikestore.data.repo.source.CommentRemoteDataSource
import io.reactivex.Single

class CommentRepositoryImp(val remoteDataSource: CommentDataSource):CommentRepository {
    override fun getAll(productId: Int): Single<List<Comment>> {
        return remoteDataSource.getAll(productId)
    }

    override fun insert(comment: Comment): Single<Comment> {
        TODO("Not yet implemented")
    }
}