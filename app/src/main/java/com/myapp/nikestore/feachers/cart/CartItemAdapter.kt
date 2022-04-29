package com.myapp.nikestore.feachers.cart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.myapp.nikestore.NikeViewModel
import com.myapp.nikestore.R
import com.myapp.nikestore.common.formatPrice
import com.myapp.nikestore.services.ImageLoadingService
import com.myapp.nikestore.view.NikeImageView
import com.sevenlearn.nikestore.data.CartItem
import com.sevenlearn.nikestore.data.PurchaseDetail

const val VIEWTYPE_CART_ITEM=0
const val VIEWTYPE_PURCHASE_DETAIL=1
class CartItemAdapter(
    val cartItems:MutableList<CartItem>,
    val imageLoadingService: ImageLoadingService,
    val cartItemViewCallbacks:CartItemViewCallbacks
    ): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var purchaseDetail: PurchaseDetail? = null

    fun removeFromCart(cartItem: CartItem){
        val index=cartItems.indexOf(cartItem)
        if(index>-1){
            cartItems.removeAt(index)
            notifyItemRemoved(index)
        }
    }
    fun increaseCartItemCount(cartItem: CartItem){
        val index=cartItems.indexOf(cartItem)
        if(index>-1){
            cartItems[index].changeCountPrograssBarIsVisible=false
            notifyItemChanged(index)
        }
    }
    fun decreaseCartItemCount(cartItem: CartItem){
        val index=cartItems.indexOf(cartItem)
        if(index>-1){
            cartItems[index].changeCountPrograssBarIsVisible=false
            notifyItemChanged(index)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType== VIEWTYPE_CART_ITEM){
            return CartItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_cart,parent,false))
        }
        else{
            return PurchaseDetailViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_purchase_detail,parent,false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CartItemViewHolder){
            holder.bindCartItem(cartItems[position])
        }
        else if (holder is PurchaseDetailViewHolder){
            purchaseDetail?.let {
                holder.bindPurchaseDetail(it.total_price,it.shipping_cost,it.payable_price)

            }
        }
    }

    override fun getItemCount(): Int {
      return cartItems.size+1
    }

    override fun getItemViewType(position: Int): Int {
        if(position==cartItems.size){
            return VIEWTYPE_PURCHASE_DETAIL
        }
        else return VIEWTYPE_CART_ITEM
    }

    inner class CartItemViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val productIv_Cart:NikeImageView=itemView.findViewById(R.id.productIv_Cart)
        val productTitleTv_Cart:TextView=itemView.findViewById(R.id.productTitleTv_Cart)
        val cartItemCountTv:TextView=itemView.findViewById(R.id.cartItemCountTv)
        val increaseBtn_Cart:ImageView=itemView.findViewById(R.id.increaseBtn_Cart)
        val decreaseBtn_Cart:ImageView=itemView.findViewById(R.id.decreaseBtn_Cart)
        val previousPrice_Cart:TextView=itemView.findViewById(R.id.previousPrice_Cart)
        val currentPrice_Cart:TextView=itemView.findViewById(R.id.currentPrice_Cart)
        val removeFromCartBtn:TextView=itemView.findViewById(R.id.removeFromCartBtn)
        val changeCountPb:ProgressBar=itemView.findViewById(R.id.changeCountPb)


        fun bindCartItem(cartItem: CartItem){
            imageLoadingService.load(productIv_Cart,cartItem.product.image)
            productTitleTv_Cart.text=cartItem.product.title
            cartItemCountTv.text=cartItem.count.toString()
            previousPrice_Cart.text= formatPrice(cartItem.product.price + cartItem.product.discount)
            currentPrice_Cart.text= formatPrice(cartItem.product.price)

            removeFromCartBtn.setOnClickListener {
                cartItemViewCallbacks.onRemoveCartItemButtonClick(cartItem)
            }

            productIv_Cart.setOnClickListener {
                cartItemViewCallbacks.onProductImageClick(cartItem)
            }

            changeCountPb.visibility=if(cartItem.changeCountPrograssBarIsVisible) View.VISIBLE else View.GONE
            cartItemCountTv.visibility=if(cartItem.changeCountPrograssBarIsVisible) View.INVISIBLE else View.VISIBLE

            increaseBtn_Cart.setOnClickListener {
                changeCountPb.visibility=View.VISIBLE
                cartItem.changeCountPrograssBarIsVisible=true
                cartItemCountTv.visibility=View.INVISIBLE
                cartItemViewCallbacks.onIncreaseCartItemButtonClick(cartItem)
            }

            decreaseBtn_Cart.setOnClickListener {
                if(cartItem.count>1){
                    changeCountPb.visibility=View.VISIBLE
                    cartItem.changeCountPrograssBarIsVisible=true
                    cartItemCountTv.visibility=View.INVISIBLE
                    cartItemViewCallbacks.onDecreaseCartItemButtonClick(cartItem)
                }

            }


        }
    }

     class PurchaseDetailViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val totalPriceTv:TextView=itemView.findViewById(R.id.totalPriceTv_purchaseDetail)
        val shippingCostTv:TextView=itemView.findViewById(R.id.shippingCost_purchaseDetail)
        val payablePriceTv:TextView=itemView.findViewById(R.id.payablePrice_purchaseDetail)

        fun bindPurchaseDetail(totalPrice:Int,shippingCost:Int,payablePrice:Int ){
            totalPriceTv.text= formatPrice(totalPrice)
            shippingCostTv.text= formatPrice(shippingCost)
            payablePriceTv.text= formatPrice(payablePrice)
        }
    }

    interface CartItemViewCallbacks{
        fun onRemoveCartItemButtonClick(cartItem: CartItem)
        fun onIncreaseCartItemButtonClick(cartItem: CartItem)
        fun onDecreaseCartItemButtonClick(cartItem: CartItem)
        fun onProductImageClick(cartItem: CartItem)

    }




}