package com.myapp.nikestore.feachers.cart

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.myapp.nikestore.NikeFragment
import com.myapp.nikestore.R
import com.myapp.nikestore.common.EXTRA_KEY_DATA
import com.myapp.nikestore.common.NikeCompletableObserver
import com.myapp.nikestore.feachers.auth.AuthActivity
import com.myapp.nikestore.feachers.shipping.ShippingActivity
import com.myapp.nikestore.product.ProductDetailActivity
import com.myapp.nikestore.services.ImageLoadingService
import com.sevenlearn.nikestore.data.CartItem
import com.sevenlearn.nikestore.data.PurchaseDetail
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class CartFragment: NikeFragment(),CartItemAdapter.CartItemViewCallbacks {
    val cartViewModel:CartViewModel by inject()
    val imageLoadingService:ImageLoadingService by inject()
    var cartItemAdapter:CartItemAdapter?=null
    lateinit var cartItemRv:RecyclerView
    val compositeDisposable=CompositeDisposable()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cart,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cartItemRv=view.findViewById(R.id.cartItemsRv)
        val payBtn=view.findViewById<ExtendedFloatingActionButton>(R.id.payBtn)

        cartViewModel.prograssLiveData.observe(viewLifecycleOwner){
            setPrograssBarIndicator(it)
        }
        cartViewModel.cartItemsLiveData.observe(viewLifecycleOwner){
            Timber.i(it.toString())
            cartItemRv.layoutManager=LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)
            cartItemAdapter= CartItemAdapter(it as MutableList,imageLoadingService,this)
            cartItemRv.adapter=cartItemAdapter

        }
        cartViewModel.purchaseDetailLiveData.observe(viewLifecycleOwner){
            Timber.i(it.toString())
            cartItemAdapter?.let {  adapter->
                adapter.purchaseDetail=it
                adapter.notifyItemChanged(adapter.cartItems.size)
            }

        }
        cartViewModel.emptyStateLiveData.observe(viewLifecycleOwner){
            val emptyState=showEmptyState(R.layout.view_cart_empty_state)
            if(it.mustShow){

                emptyState?.let { view->
                    val cartEmptyStateTv=view.findViewById<TextView>(R.id.cartEmptyStateTv)
                    cartEmptyStateTv.text=getString(it.messageResId)
                    val cartEmptyStateCTABtn=view.findViewById<MaterialButton>(R.id.cartEmptyStateCTABtn)
                    cartEmptyStateCTABtn.visibility=if(it.mustShowCallToActionBtn) View.VISIBLE else View.GONE
                    cartEmptyStateCTABtn.setOnClickListener {
                        startActivity(Intent(requireContext(),AuthActivity::class.java))
                    }
                }

            }
            else{
                emptyState?.let {
                    val emptyStateRootView= it.findViewById<FrameLayout>(R.id.emptyStateRootView)
                    emptyStateRootView.visibility=View.GONE

                }
            }
        }


        payBtn.setOnClickListener {
            startActivity(Intent(requireContext(),ShippingActivity::class.java).apply {
                putExtra(EXTRA_KEY_DATA,cartViewModel.purchaseDetailLiveData.value)
            })
        }

    }

    override fun onStart() {
        super.onStart()
        cartViewModel.refresh()
    }

    override fun onRemoveCartItemButtonClick(cartItem: CartItem) {
        cartViewModel.removeFromCart(cartItem)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object:NikeCompletableObserver(compositeDisposable){
                override fun onComplete() {
                    cartItemAdapter?.removeFromCart(cartItem)
                }

            })
    }

    override fun onIncreaseCartItemButtonClick(cartItem: CartItem) {
        cartViewModel.increaseCartItemCount(cartItem)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object:NikeCompletableObserver(compositeDisposable){
                override fun onComplete() {
                    cartItemAdapter?.increaseCartItemCount(cartItem)
                }

            })
    }

    override fun onDecreaseCartItemButtonClick(cartItem: CartItem) {
        cartViewModel.decreaseCartItemCount(cartItem)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object:NikeCompletableObserver(compositeDisposable){
                override fun onComplete() {
                    cartItemAdapter?.decreaseCartItemCount(cartItem)
                }

            })
    }

    override fun onProductImageClick(cartItem: CartItem) {
        val intent=Intent(requireContext(),ProductDetailActivity::class.java)
        intent.putExtra(EXTRA_KEY_DATA,cartItem.product)
        startActivity(intent)
    }

}