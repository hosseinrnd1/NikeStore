package com.myapp.nikestore.feachers.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.myapp.nikestore.R
import com.myapp.nikestore.common.EXTRA_KEY_DATA
import com.myapp.nikestore.data.Banner
import com.myapp.nikestore.services.FrescoImageLoadingService
import com.myapp.nikestore.services.ImageLoadingService
import com.myapp.nikestore.view.NikeImageView
import org.koin.android.ext.android.inject
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException

class BannerFragment:Fragment() {
    val imageloadingfService: ImageLoadingService by inject()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val imageview=inflater.inflate(R.layout.fragment_banner,container,false) as NikeImageView
        val banner=requireArguments().getParcelable<Banner>(EXTRA_KEY_DATA)?:throw IllegalStateException("banner can not be null")
        imageloadingfService.load(imageview,banner.image)

        return imageview
    }


    companion object{
        fun newInstance(banner: Banner):Fragment{

          return  BannerFragment().apply {
                arguments=Bundle().apply {
                    putParcelable(EXTRA_KEY_DATA,banner)
                }

            }

        }
    }
}
