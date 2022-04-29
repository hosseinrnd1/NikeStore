package com.myapp.nikestore.data.repo

import com.myapp.nikestore.data.TokenResponse
import io.reactivex.Completable

interface UserRepository {

    fun login(userName:String,password:String):Completable

    fun signUp(userName:String,password:String):Completable

    fun loadToken()

    fun getUserName():String

    fun signOut()



}