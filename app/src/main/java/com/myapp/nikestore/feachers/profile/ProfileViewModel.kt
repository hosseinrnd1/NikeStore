package com.myapp.nikestore.feachers.profile

import com.myapp.nikestore.NikeViewModel
import com.myapp.nikestore.data.TokenContainer
import com.myapp.nikestore.data.repo.UserRepository

class ProfileViewModel(private val userRepository: UserRepository):NikeViewModel() {
    val userName:String
    get() = userRepository.getUserName()

    val isSignedIn:Boolean
    get() = TokenContainer.token!=null

    fun signOut(){
        userRepository.signOut()
    }
}