package com.myapp.nikestore.data

import timber.log.Timber

object TokenContainer {
    var token:String?=null
    private set
    var refreshToken:String?=null
    private set

    fun update(token:String?,refreshToken:String?){
        Timber.i("access token -> ${token?.substring(0,10)},Refresh Token -> $refreshToken")
        this.token=token
        this.refreshToken=refreshToken
    }

}