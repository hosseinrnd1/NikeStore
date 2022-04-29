package com.myapp.nikestore.feachers.favorites

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.myapp.nikestore.NikeActivity
import com.myapp.nikestore.R
import com.myapp.nikestore.common.EXTRA_KEY_DATA
import com.myapp.nikestore.data.Product
import com.myapp.nikestore.product.ProductDetailActivity
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteProductsActivity : NikeActivity(),FavoriteProductsAdapter.FavoriteProductsEventListener {
    val favoriteProductsViewModel:FavoriteProductsViewModel by viewModel()
    lateinit var favoriteProductsRv:RecyclerView
    lateinit var favoriteProductsAdapter:FavoriteProductsAdapter
    lateinit var infoBtn_Favorites:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_products)
        favoriteProductsRv=findViewById(R.id.favoriteProductsRv)
        infoBtn_Favorites=findViewById(R.id.infoBtn_Favorites)

        infoBtn_Favorites.setOnClickListener {
        Snackbar.make(it,getString(R.string.help_delete_favorite),Snackbar.LENGTH_LONG).apply {
            setBackgroundTint(R.attr.colorPrimary)
            show()
        }
        }

        favoriteProductsViewModel.favoriteProductLiveData.observe(this){
            if(it.isNotEmpty()){
                favoriteProductsRv.layoutManager=LinearLayoutManager(this,RecyclerView.VERTICAL,false)
                favoriteProductsAdapter= FavoriteProductsAdapter(it as MutableList,get(),this)
                favoriteProductsRv.adapter=favoriteProductsAdapter
            }
            else{
                val emptystateRootView=showEmptyState(R.layout.view_default_empty_state)
                val emptyStateMessageTv:TextView=emptystateRootView!!.findViewById(R.id.emptyStateMessageTv)
                emptyStateMessageTv.text=getString(R.string.default_empty_state_titleMessage)
            }

        }



    }

    override fun onClick(product: Product) {
        startActivity(Intent(this,ProductDetailActivity::class.java).apply {
            putExtra(EXTRA_KEY_DATA,product)
        })
    }

    override fun onLongClickListener(product: Product) {
        favoriteProductsViewModel.removeFromFavorites(product)

    }
}