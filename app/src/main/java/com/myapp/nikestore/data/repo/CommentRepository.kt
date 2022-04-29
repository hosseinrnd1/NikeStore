package com.myapp.nikestore.data.repo

import com.myapp.nikestore.data.Comment
import io.reactivex.Single


interface CommentRepository {
    fun getAll(productId:Int): Single<List<Comment>>
    fun insert(comment: Comment):Single<Comment>
}