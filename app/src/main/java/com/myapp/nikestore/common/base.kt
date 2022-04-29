package com.myapp.nikestore

import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.os.Message
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.children
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.snackbar.Snackbar
import com.myapp.nikestore.common.NikeException
import com.myapp.nikestore.common.NikeExceptionMapper
import com.myapp.nikestore.feachers.auth.AuthActivity
import io.reactivex.disposables.CompositeDisposable
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.IllegalStateException
import java.time.Duration

abstract class NikeActivity:NikeView,AppCompatActivity(){
    override val rootView: CoordinatorLayout?
        get() {
           val viewGroup=findViewById(android.R.id.content) as ViewGroup
            if(viewGroup !is CoordinatorLayout){
                viewGroup.children.forEach {
                    if(it is CoordinatorLayout)
                        return it
                }
                throw IllegalStateException("RootView must be Instance of Coordinator layout")

            }else{
                return viewGroup
            }
        }
    override val viewContext: Context?
        get() = this

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)

    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)

    }

}
abstract class NikeFragment:NikeView,Fragment(){
    override val rootView: CoordinatorLayout?
        get() = view as CoordinatorLayout
    override val viewContext: Context?
        get() = context

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

}
abstract class NikeViewModel:ViewModel(){
    val prograssLiveData= MutableLiveData<Boolean>()
    val compositeDisposable=CompositeDisposable()
    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

}

interface NikeView{
    val rootView:CoordinatorLayout?
    val viewContext:Context?
    fun setPrograssBarIndicator(mustShow:Boolean){

        rootView?.let {
            viewContext?.let { context->
                var loadingView=it.findViewById<View>(R.id.loadingView)
                if(loadingView==null && mustShow){
                    loadingView=LayoutInflater.from(viewContext).inflate(R.layout.loading_view,it,false)
                    it.addView(loadingView)
                }
                loadingView?.visibility=if (mustShow) View.VISIBLE else View.GONE


            }
        }

    }
    fun showEmptyState(layoutResId:Int):View?{
        rootView?.let {
            viewContext?.let {context->
                var emptyState=it.findViewById<View>(R.id.emptyStateRootView)
                if(emptyState==null){
                    emptyState=LayoutInflater.from(context).inflate(layoutResId,it,false)
                    it.addView(emptyState)
                }
                emptyState?.visibility=View.VISIBLE
                return  emptyState

            }
        }
        return null

    }


    fun showSnackBar(message: String,duration:Int=Snackbar.LENGTH_SHORT){
        rootView?.let {
            Snackbar.make(it,message,duration).show()
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun showError(nikeException: NikeException){
        viewContext?.let {
            when(nikeException.type){
                NikeException.Type.SIMPLE->{
                    showSnackBar(nikeException.serverMessage?:it.getString(nikeException.userFriendlyMessage))
                }

                NikeException.Type.AUTH->{
                    it.startActivity(Intent(it,AuthActivity::class.java))
                    Toast.makeText(it,nikeException.serverMessage,Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}