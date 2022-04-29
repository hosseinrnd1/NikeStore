package com.myapp.nikestore.services

import android.widget.ImageView

interface ImageLoadingService {
    fun load(image:ImageView,url:String)
}