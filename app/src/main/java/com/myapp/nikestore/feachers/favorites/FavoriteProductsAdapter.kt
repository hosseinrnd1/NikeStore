package com.myapp.nikestore.feachers.favorites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.myapp.nikestore.R
import com.myapp.nikestore.data.Product
import com.myapp.nikestore.services.ImageLoadingService
import com.myapp.nikestore.view.NikeImageView

class FavoriteProductsAdapter(
    val favoriteProducts:MutableList<Product>
    ,val imageLoadingService: ImageLoadingService,
    val favoriteProductsEventListener:FavoriteProductsEventListener
): RecyclerView.Adapter<FavoriteProductsAdapter.FavoriteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        return FavoriteViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_favorite_products,parent,false))
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(favoriteProducts[position])
    }

    override fun getItemCount(): Int {
        return favoriteProducts.size
    }

    inner class FavoriteViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val favoriteProductIv:NikeImageView=itemView.findViewById(R.id.favoriteProductIv)
        val favoriteProductTitle:TextView=itemView.findViewById(R.id.favoriteProductTitle)

        fun bind(product: Product){
           imageLoadingService.load(favoriteProductIv,product.image)
           favoriteProductTitle.text=product.title

            itemView.setOnClickListener {
                favoriteProductsEventListener.onClick(product)
            }

            itemView.setOnLongClickListener {
                favoriteProductsEventListener.onLongClickListener(product)
                favoriteProducts.remove(product)
                notifyItemRemoved(adapterPosition)
                return@setOnLongClickListener false
            }
        }
    }


    interface FavoriteProductsEventListener{
        fun onClick(product: Product)
        fun onLongClickListener(product: Product)
    }

}