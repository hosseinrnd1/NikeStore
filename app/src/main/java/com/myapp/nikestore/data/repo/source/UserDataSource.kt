package com.myapp.nikestore.data.repo.source

import com.myapp.nikestore.data.TokenResponse
import com.sevenlearn.nikestore.data.MessageResponse
import io.reactivex.Completable
import io.reactivex.Single

interface UserDataSource {

    fun login(userName:String,password:String): Single<TokenResponse>

    fun signUp(userName:String,password:String): Single<MessageResponse>

    fun loadToken()

    fun saveToken(token:String,refreshToken:String)

    fun saveUserName(userName:String)

    fun getUserName():String

    fun signOut()
}