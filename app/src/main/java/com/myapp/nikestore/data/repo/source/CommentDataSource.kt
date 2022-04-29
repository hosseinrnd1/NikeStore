package com.myapp.nikestore.data.repo.source

import com.myapp.nikestore.data.Comment
import io.reactivex.Single

interface CommentDataSource {
    fun getAll(productId:Int): Single<List<Comment>>
    fun insert(comment: Comment): Single<Comment>
}