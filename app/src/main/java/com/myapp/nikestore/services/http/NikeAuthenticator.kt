package com.myapp.nikestore.services.http

import com.google.gson.JsonObject
import com.myapp.nikestore.data.TokenContainer
import com.myapp.nikestore.data.TokenResponse
import com.myapp.nikestore.data.repo.source.CLIENT_ID
import com.myapp.nikestore.data.repo.source.CLIENT_SECRET
import com.myapp.nikestore.data.repo.source.GRANT_TYPE
import com.myapp.nikestore.data.repo.source.UserDataSource
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import timber.log.Timber

class NikeAuthenticator : Authenticator,KoinComponent {
    val apiService:ApiService by inject()
    val localDataSource:UserDataSource by inject()
    override fun authenticate(route: Route?, response: Response): Request? {

        try {
            if(TokenContainer.token!=null && TokenContainer.refreshToken!=null && !response.request.url.pathSegments.last().equals("token",false)){
                val token=refreshToken()
                if(token.isEmpty()){
                    return null
                }
                return response.request.newBuilder().header("Authorization","Bearer $token").build()
            }
        }
        catch (e:Exception){
            Timber.e(e)
        }

        return null
    }

    fun refreshToken():String{
        val response:retrofit2.Response<TokenResponse> =apiService.refreshToken(JsonObject().apply {
            addProperty("grant_type", "refresh_token")
            addProperty("refresh_token",TokenContainer.refreshToken)
            addProperty("client_id", CLIENT_ID)
            addProperty("client_secret", CLIENT_SECRET)
        }).execute()
        response.body()?.let {
            TokenContainer.update(it.access_token,it.refresh_token)
            localDataSource.saveToken(it.access_token,it.refresh_token)
            return it.access_token
        }
        return ""

    }
}