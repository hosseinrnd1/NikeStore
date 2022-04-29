package com.myapp.nikestore.data.repo.source

import com.google.gson.JsonObject
import com.myapp.nikestore.data.TokenResponse
import com.myapp.nikestore.services.http.ApiService
import com.sevenlearn.nikestore.data.MessageResponse
import io.reactivex.Single

const val GRANT_TYPE="password"
const val CLIENT_ID=2
const val CLIENT_SECRET="kyj1c9sVcksqGU4scMX7nLDalkjp2WoqQEf8PKAC"
class UserRemoteDataSource(val apiService: ApiService):UserDataSource {
    override fun login(userName: String, password: String): Single<TokenResponse> {
        return apiService.loginUser(JsonObject().apply {
            addProperty("grant_type", GRANT_TYPE)
            addProperty("client_id", CLIENT_ID)
            addProperty("client_secret", CLIENT_SECRET)
            addProperty("username", userName)
            addProperty("password", password)
        })
    }

    override fun signUp(userName: String, password: String): Single<MessageResponse> {
        return apiService.signUpUser(JsonObject().apply {
            addProperty("email", userName)
            addProperty("password", password)
        })
    }

    override fun loadToken() {
        TODO("Not yet implemented")
    }

    override fun saveToken(token: String, refreshToken: String) {
        TODO("Not yet implemented")
    }

    override fun saveUserName(userName: String) {
        TODO("Not yet implemented")
    }

    override fun getUserName(): String {
        TODO("Not yet implemented")
    }

    override fun signOut() {
        TODO("Not yet implemented")
    }
}