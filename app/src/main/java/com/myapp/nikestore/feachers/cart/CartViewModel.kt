package com.myapp.nikestore.feachers.cart

import androidx.lifecycle.MutableLiveData
import com.myapp.nikestore.NikeViewModel
import com.myapp.nikestore.R
import com.myapp.nikestore.common.NikeSingleObserver
import com.myapp.nikestore.common.asyncNetworkRequest
import com.myapp.nikestore.data.EmptyState
import com.myapp.nikestore.data.TokenContainer
import com.myapp.nikestore.data.repo.CartRepository
import com.sevenlearn.nikestore.data.CartItem
import com.sevenlearn.nikestore.data.CartItemCount
import com.sevenlearn.nikestore.data.CartResponse
import com.sevenlearn.nikestore.data.PurchaseDetail
import io.reactivex.Completable
import org.greenrobot.eventbus.EventBus

class CartViewModel(val cartRepositoryImp: CartRepository):NikeViewModel() {
    val cartItemsLiveData=MutableLiveData<List<CartItem>>()
    val purchaseDetailLiveData=MutableLiveData<PurchaseDetail>()
    val emptyStateLiveData=MutableLiveData<EmptyState>()
    private fun getCartItems(){
        if(!TokenContainer.token.isNullOrEmpty()){
            emptyStateLiveData.value= EmptyState(false)
            prograssLiveData.value=true
            cartRepositoryImp.get()
                .asyncNetworkRequest()
                .doFinally {
                    prograssLiveData.value=false
                }
                .subscribe(object:NikeSingleObserver<CartResponse>(compositeDisposable){
                    override fun onSuccess(t: CartResponse) {
                        if(t.cart_items.isNotEmpty()){
                            cartItemsLiveData.value=t.cart_items
                            purchaseDetailLiveData.value= PurchaseDetail(t.total_price,t.shipping_cost,t.payable_price)
                        }
                        else{
                            emptyStateLiveData.value= EmptyState(true, R.string.cartEmptyState)
                        }

                    }

                })
        }
        else{
            emptyStateLiveData.value= EmptyState(true,R.string.cartEmptyStateLoginRequired,true)

        }

    }

    fun removeFromCart(cartItem: CartItem):Completable{
        return cartRepositoryImp.remove(cartItem.cart_item_id)
            .doAfterSuccess {
                calculateAndPublishPurchaseDetail()
                cartItemsLiveData.value?.let {
                    if(it.isEmpty())
                        emptyStateLiveData.postValue(EmptyState(true,R.string.cartEmptyState))
                }
                val cartItemCount=EventBus.getDefault().getStickyEvent(CartItemCount::class.java)
                cartItemCount?.let {
                    it.count-=cartItem.count
                    EventBus.getDefault().postSticky(it)
                }

            }
            .ignoreElement()
    }

    fun increaseCartItemCount(cartItem: CartItem):Completable{
        return cartRepositoryImp.changeCount(cartItem.cart_item_id,++cartItem.count)
            .doAfterSuccess {
                calculateAndPublishPurchaseDetail()
                val cartItemCount=EventBus.getDefault().getStickyEvent(CartItemCount::class.java)
                cartItemCount?.let {
                    it.count+=1
                    EventBus.getDefault().postSticky(it)
                }
            }
            .ignoreElement()
    }

    fun decreaseCartItemCount(cartItem: CartItem):Completable{
        return cartRepositoryImp.changeCount(cartItem.cart_item_id,--cartItem.count)
            .doAfterSuccess {
                calculateAndPublishPurchaseDetail()
                val cartItemCount=EventBus.getDefault().getStickyEvent(CartItemCount::class.java)
                cartItemCount?.let {
                    it.count-=1
                    EventBus.getDefault().postSticky(it)
                }
            }
            .ignoreElement()
    }

    fun refresh(){
        getCartItems()
    }

    private fun calculateAndPublishPurchaseDetail(){


        cartItemsLiveData.value?.let { cartItems->
            purchaseDetailLiveData.value?.let { purchaseDetail->
                var totalPrice=0
                var payablePrice=0

                cartItems.forEach{ cartItem->
                    totalPrice+=(cartItem.product.price)*cartItem.count
                    payablePrice+=(cartItem.product.price-cartItem.product.discount)*cartItem.count
                }
                purchaseDetail.total_price=totalPrice
                purchaseDetail.payable_price=payablePrice
                purchaseDetailLiveData.postValue(purchaseDetail)

            }
        }

    }


}