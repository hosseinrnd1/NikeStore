package com.myapp.nikestore.product.comment

import androidx.lifecycle.MutableLiveData
import com.myapp.nikestore.NikeViewModel
import com.myapp.nikestore.common.NikeSingleObserver
import com.myapp.nikestore.common.asyncNetworkRequest
import com.myapp.nikestore.data.Comment
import com.myapp.nikestore.data.repo.CommentRepository

class CommentListViewModel(productId:Int,commentRepository: CommentRepository) : NikeViewModel() {
    val commentLiveData=MutableLiveData<List<Comment>>()
    init {
        prograssLiveData.value=true
        commentRepository.getAll(productId)
            .asyncNetworkRequest()
            .doFinally { prograssLiveData.value=false }
            .subscribe(object :NikeSingleObserver<List<Comment>>(compositeDisposable){
                override fun onSuccess(t: List<Comment>) {
                    commentLiveData.value=t
                }

            })

    }

}