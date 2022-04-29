package com.myapp.nikestore.product

import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.myapp.nikestore.NikeActivity
import com.myapp.nikestore.R
import com.myapp.nikestore.common.EXTRA_KEY_DATA
import com.myapp.nikestore.common.EXTRA_KEY_ID
import com.myapp.nikestore.common.NikeCompletableObserver
import com.myapp.nikestore.common.formatPrice
import com.myapp.nikestore.data.Comment
import com.myapp.nikestore.data.Product
import com.myapp.nikestore.product.comment.CommentListActivity
import com.myapp.nikestore.services.ImageLoadingService
import com.myapp.nikestore.view.NikeImageView
import com.myapp.nikestore.view.scroll.ObservableScrollView
import com.myapp.nikestore.view.scroll.ObservableScrollViewCallbacks
import com.myapp.nikestore.view.scroll.ScrollState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber
import java.util.ArrayList

class ProductDetailActivity : NikeActivity() {
    val productDetailViewModel:ProductDetailViewModel by viewModel { parametersOf(intent.extras)}
    val imageLoadingService:ImageLoadingService by inject()
    val compositeDisposable=CompositeDisposable()
    lateinit var productIv_Detail: NikeImageView
    lateinit var productTitleTv_Detail: TextView
    lateinit var previousPriceTv_Detail: TextView
    lateinit var currentPriceTv_Detail: TextView
    lateinit var observableScrollView: ObservableScrollView
    lateinit var toolbarView:MaterialCardView
    lateinit var toolbarTitleTv:TextView
    lateinit var commentRv:RecyclerView
    lateinit var commentAdapter: CommentAdapter
    lateinit var viewAllCommentBtn:MaterialButton
    lateinit var addTocartBtn:ExtendedFloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        productIv_Detail=findViewById(R.id.productIv_Detail)
        productTitleTv_Detail=findViewById(R.id.productTitleTv_Detail)
        previousPriceTv_Detail=findViewById(R.id.previousPriceTv_Detail)
        currentPriceTv_Detail=findViewById(R.id.currentPriceTv_Detail)
        toolbarTitleTv=findViewById(R.id.toolbarTitleTv_Detail)
        viewAllCommentBtn=findViewById(R.id.viewAllCommentBtn)
        addTocartBtn=findViewById(R.id.addToCartBtn)

        productDetailViewModel.productLiveData.observe(this){
            imageLoadingService.load(productIv_Detail,it.image)
            productTitleTv_Detail.text=it.title
            previousPriceTv_Detail.text= formatPrice(it.previous_price)
            previousPriceTv_Detail.paintFlags= Paint.STRIKE_THRU_TEXT_FLAG
            currentPriceTv_Detail.text= formatPrice(it.price)
            toolbarTitleTv.text=it.title

        }
        productDetailViewModel.commentsLiveData.observe(this){
            commentAdapter.comments=it as ArrayList<Comment>
            if(it.size>3){
                viewAllCommentBtn.visibility=View.VISIBLE

                viewAllCommentBtn.setOnClickListener {
                    startActivity(Intent(this,CommentListActivity::class.java).apply {
                        putExtra(EXTRA_KEY_ID,productDetailViewModel.productLiveData.value!!.id)
                    })
                }
            }
        }
        productDetailViewModel.prograssLiveData.observe(this){
            setPrograssBarIndicator(it)
        }

        initView()













    }
    fun initView(){
        commentRv=findViewById(R.id.commentsRv)
        commentRv.layoutManager=LinearLayoutManager(this,RecyclerView.VERTICAL,false)
        commentAdapter= CommentAdapter()
        commentRv.adapter=commentAdapter

        productIv_Detail.post {
            observableScrollView=findViewById(R.id.observableScrollView)
            toolbarView=findViewById(R.id.toolbarView)
            val productIvHeight=productIv_Detail.height
            observableScrollView.addScrollViewCallbacks(object:ObservableScrollViewCallbacks{
                override fun onScrollChanged(scrollY: Int, firstScroll: Boolean, dragging: Boolean) {
                    toolbarView.alpha = scrollY.toFloat() / productIvHeight.toFloat()
                    Timber.i("$productIvHeight")
                    productIv_Detail.translationY=(scrollY /2).toFloat()

                }

                override fun onDownMotionEvent() {

                }

                override fun onUpOrCancelMotionEvent(scrollState: ScrollState?) {

                }

            })
        }

        addTocartBtn.setOnClickListener {
            productDetailViewModel.onAddToCartBtn()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object:NikeCompletableObserver(compositeDisposable){
                    override fun onComplete() {
                        showSnackBar(getString(R.string.addToCart_success))
                    }

                })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}
