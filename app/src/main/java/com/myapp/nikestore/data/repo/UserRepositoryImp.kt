package com.myapp.nikestore.data.repo

import com.myapp.nikestore.data.TokenContainer
import com.myapp.nikestore.data.TokenResponse
import com.myapp.nikestore.data.repo.source.ProductLocalDataSource
import com.myapp.nikestore.data.repo.source.UserDataSource
import com.myapp.nikestore.data.repo.source.UserLocalDataSource
import com.myapp.nikestore.data.repo.source.UserRemoteDataSource
import io.reactivex.Completable

class UserRepositoryImp(val userLocalDataSource:UserDataSource,val userRemoteDataSource: UserDataSource):UserRepository {
    override fun login(userName: String, password: String): Completable {
         return userRemoteDataSource.login(userName, password).doOnSuccess {
             onSuccessFullLogin(it,userName)
         }.ignoreElement()
    }

    override fun signUp(userName: String, password: String): Completable {
        return userRemoteDataSource.signUp(userName,password).flatMap {
            userRemoteDataSource.login(userName,password).doOnSuccess {
                onSuccessFullLogin(it,userName)
            }

        }.ignoreElement()
    }

    override fun loadToken() {
        userLocalDataSource.loadToken()
    }

    override fun getUserName():String {
        return userLocalDataSource.getUserName()
    }

    override fun signOut() {
         userLocalDataSource.signOut()
         TokenContainer.update(null,null)
    }


    fun onSuccessFullLogin(tokenResponse: TokenResponse,userName:String){
        TokenContainer.update(tokenResponse.access_token,tokenResponse.refresh_token)
        userLocalDataSource.saveToken(tokenResponse.access_token,tokenResponse.refresh_token)
        userLocalDataSource.saveUserName(userName)
    }
}