package com.myapp.nikestore.feachers.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.myapp.nikestore.R

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentcontainer_auth,Fragment_SignUp())
            commit()
        }
    }
}