package com.myapp.nikestore.services

import android.widget.ImageView
import com.facebook.drawee.view.SimpleDraweeView
import java.lang.IllegalStateException

class FrescoImageLoadingService : ImageLoadingService {
    override fun load(image: ImageView, url: String) {
        if(image is SimpleDraweeView){
            image.setImageURI(url)
        }
        else{
            throw IllegalStateException("must be instance of SimpleDrawView")
        }
    }

}