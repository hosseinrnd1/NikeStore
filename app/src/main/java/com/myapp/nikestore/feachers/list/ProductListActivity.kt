package com.myapp.nikestore.feachers.list

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.myapp.nikestore.NikeActivity
import com.myapp.nikestore.R
import com.myapp.nikestore.common.EXTRA_KEY_DATA
import com.myapp.nikestore.data.Product
import com.myapp.nikestore.feachers.common.ProductListAdapter
import com.myapp.nikestore.product.ProductDetailActivity
import com.myapp.nikestore.view.NikeToolbar
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

const val VIEW_TYPE_ROUND=0
const val VIEW_TYPE_SMALL=1
const val VIEW_TYPE_LARGE=2

class ProductListActivity : NikeActivity(),ProductListAdapter.ProductEventListener{
    val productListViewModel:ProductListViewModel by inject{ parametersOf(intent.extras!!.getInt(
        EXTRA_KEY_DATA))}
    val productListAdapter:ProductListAdapter by inject{ parametersOf(VIEW_TYPE_SMALL)}
    lateinit var productListRv:RecyclerView
    lateinit var viewTypeChangerBtn:ImageView
    lateinit var sortBtn: View
    lateinit var tv_sortWith:TextView
    lateinit var toolbar_product_list:NikeToolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)
        productListRv=findViewById(R.id.productListRv)
        viewTypeChangerBtn=findViewById(R.id.iv_viewTypeChangerBtn)
        sortBtn=findViewById(R.id.sortBtn)
        tv_sortWith=findViewById(R.id.tv_sortWith)
        toolbar_product_list=findViewById(R.id.toolbar_product_list)

        productListAdapter.productEventListener=this

        val gridLayoutManager=GridLayoutManager(this,2)
        productListRv.layoutManager=gridLayoutManager
        productListRv.adapter=productListAdapter

        viewTypeChangerBtn.setOnClickListener {
            if(productListAdapter.viewType== VIEW_TYPE_SMALL){
                productListAdapter.viewType=VIEW_TYPE_LARGE
                viewTypeChangerBtn.setImageResource(R.drawable.ic_viewtype_large)
                gridLayoutManager.spanCount=1
                productListAdapter.notifyDataSetChanged()
            }
            else{
                productListAdapter.viewType= VIEW_TYPE_SMALL
                viewTypeChangerBtn.setImageResource(R.drawable.ic_grid)
                gridLayoutManager.spanCount=2
                productListAdapter.notifyDataSetChanged()
            }
        }
        productListViewModel.selectedSortTitleLiveData.observe(this){
            tv_sortWith.text=getString(it)
        }
        productListViewModel.prograssLiveData.observe(this){
            setPrograssBarIndicator(it)
        }

        toolbar_product_list.onBackButtonClickListener=View.OnClickListener {
            finish()
        }


        sortBtn.setOnClickListener {
            val dialog=MaterialAlertDialogBuilder(this)
                .setSingleChoiceItems(R.array.sortTitlesArray,productListViewModel.sort,object :DialogInterface.OnClickListener{
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        productListViewModel.onSelectedSortChangeByUser(p1)
                        p0!!.dismiss()
                    }

                })
                .setTitle(R.string.sort)
                dialog.show()

        }


        productListViewModel.productLiveData.observe(this){
            productListAdapter.products=it as ArrayList<Product>
        }


    }

    override fun onProductClick(product: Product) {
        startActivity(Intent(this,ProductDetailActivity::class.java).apply {
            putExtra(EXTRA_KEY_DATA,product)
        })
    }

    override fun onFavoriteBtnClick(product: Product) {
        productListViewModel.addProductToFavorite(product)
    }
}