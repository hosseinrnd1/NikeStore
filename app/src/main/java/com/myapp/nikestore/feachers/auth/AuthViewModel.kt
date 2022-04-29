package com.myapp.nikestore.feachers.auth

import com.myapp.nikestore.NikeViewModel
import com.myapp.nikestore.data.TokenResponse
import com.myapp.nikestore.data.repo.UserRepository
import io.reactivex.Completable
import io.reactivex.Single

class AuthViewModel(private val userRepository: UserRepository): NikeViewModel() {

    fun loginUser(username:String,password:String): Completable{
        prograssLiveData.value=true
        return userRepository.login(username,password).doFinally{
            prograssLiveData.postValue(false)
        }
    }

    fun signUpUser(username:String,password:String): Completable{
        prograssLiveData.value=true
        return userRepository.signUp(username,password).doFinally {
            prograssLiveData.postValue(false)

        }
    }

}