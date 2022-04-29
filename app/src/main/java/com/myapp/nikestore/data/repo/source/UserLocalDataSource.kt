package com.myapp.nikestore.data.repo.source

import android.content.SharedPreferences
import com.myapp.nikestore.data.TokenContainer
import com.myapp.nikestore.data.TokenResponse
import com.sevenlearn.nikestore.data.MessageResponse
import io.reactivex.Single

class UserLocalDataSource(val sharedPreferences: SharedPreferences):UserDataSource {
    override fun login(userName: String, password: String): Single<TokenResponse> {
        TODO("Not yet implemented")
    }

    override fun signUp(userName: String, password: String): Single<MessageResponse> {
        TODO("Not yet implemented")
    }

    override fun loadToken() {
        TokenContainer.update(
            sharedPreferences.getString("access_token",null),
            sharedPreferences.getString("refresh_token",null)
        )
    }

    override fun saveToken(token: String, refreshToken: String) {
        sharedPreferences.edit().apply {
            putString("access_token",token)
            putString("refresh_token",refreshToken)
        }.apply()
    }

    override fun saveUserName(userName: String) {
        sharedPreferences.edit().apply {
            putString("user_name",userName)
        }.apply()
    }

    override fun getUserName(): String {
        return sharedPreferences.getString("user_name","")?:""
    }

    override fun signOut() {
        sharedPreferences.edit().apply {
            clear()
        }.apply()
    }
}