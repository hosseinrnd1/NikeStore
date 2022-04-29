package com.myapp.nikestore.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import com.myapp.nikestore.R

class NikeToolbar(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {
     var toolbarTitleTv:TextView
     var backBtn:ImageView
     var onBackButtonClickListener: View.OnClickListener?=null
    set(value) {
        field=value
        backBtn.setOnClickListener(onBackButtonClickListener)
    }

    init {
        inflate(context, R.layout.view_toolbar,this)
        toolbarTitleTv=findViewById(R.id.toolbarTitleTv)
        backBtn=findViewById(R.id.backBtn_toolbar)
        if(attrs!=null){
            val a=context.obtainStyledAttributes(attrs,R.styleable.NikeToolbar)
            val title=a.getString(R.styleable.NikeToolbar_title_nt)
            if(title!=null && title.isNotEmpty()){
                toolbarTitleTv.text=title
            }
            a.recycle()

        }
    }
}