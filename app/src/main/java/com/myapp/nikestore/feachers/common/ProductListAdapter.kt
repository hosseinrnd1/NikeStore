package com.myapp.nikestore.feachers.common

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.myapp.nikestore.R
import com.myapp.nikestore.common.formatPrice
import com.myapp.nikestore.common.implementSpringAnimationTrait
import com.myapp.nikestore.data.Product
import com.myapp.nikestore.feachers.list.VIEW_TYPE_LARGE
import com.myapp.nikestore.feachers.list.VIEW_TYPE_ROUND
import com.myapp.nikestore.feachers.list.VIEW_TYPE_SMALL
import com.myapp.nikestore.services.ImageLoadingService
import com.myapp.nikestore.view.NikeImageView
import java.lang.IllegalStateException


class ProductListAdapter(var viewType: Int, val imageLoadingService: ImageLoadingService) : RecyclerView.Adapter<ProductListAdapter.ProductViewHolder>() {

    var productEventListener: ProductEventListener?=null
    var products=ArrayList<Product>()
    set(value) {
        field=value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val layoutResId=when(viewType){
            VIEW_TYPE_ROUND->R.layout.item_product
            VIEW_TYPE_SMALL->R.layout.item_product_small
            VIEW_TYPE_LARGE->R.layout.item_product_large
            else->throw IllegalStateException("ViewType not valid")
        }
        return ProductViewHolder(LayoutInflater.from(parent.context).inflate(layoutResId,parent,false))
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bindProduct(products[position])
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun getItemViewType(position: Int): Int {
        return viewType
    }



    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val iv_product=itemView.findViewById<NikeImageView>(R.id.productIv)
        val tv_productTitle=itemView.findViewById<TextView>(R.id.productTitleTv)
        val tv_previousPrice=itemView.findViewById<TextView>(R.id.previousPriceTv)
        val tv_currentPrice=itemView.findViewById<TextView>(R.id.currentPriceTv)
        val favoriteBtn=itemView.findViewById<ImageView>(R.id.favoriteBtn)
        fun bindProduct(product: Product){
            imageLoadingService.load(iv_product,url = product.image)
            tv_productTitle.text=product.title
            tv_previousPrice.text= formatPrice(product.previous_price)
            tv_previousPrice.paintFlags=Paint.STRIKE_THRU_TEXT_FLAG
            tv_currentPrice.text= formatPrice(product.price)

            itemView.implementSpringAnimationTrait()
            itemView.setOnClickListener {
                productEventListener?.onProductClick(product)
            }

            if(product.isFavorite){
                favoriteBtn.setImageResource(R.drawable.ic_favorite_fill)
            }
            else{
                favoriteBtn.setImageResource(R.drawable.ic_favorites)
            }
            favoriteBtn.setOnClickListener {
                productEventListener?.onFavoriteBtnClick(product)
                product.isFavorite=!product.isFavorite
                notifyItemChanged(adapterPosition)

            }

        }
    }

    interface ProductEventListener{
        fun onProductClick(product: Product)
        fun onFavoriteBtnClick(product: Product)

    }

}