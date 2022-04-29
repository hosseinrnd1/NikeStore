package com.myapp.nikestore.feachers.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.button.MaterialButton
import com.myapp.nikestore.NikeFragment
import com.myapp.nikestore.R
import com.myapp.nikestore.common.EXTRA_KEY_DATA
import com.myapp.nikestore.common.EXTRA_KEY_ID
import com.myapp.nikestore.common.convertDpToPx
import com.myapp.nikestore.data.Product
import com.myapp.nikestore.data.SORT_LATEST
import com.myapp.nikestore.feachers.main.BannerSliderAdapter
import com.myapp.nikestore.feachers.common.ProductListAdapter
import com.myapp.nikestore.feachers.list.ProductListActivity
import com.myapp.nikestore.feachers.list.VIEW_TYPE_ROUND
import com.myapp.nikestore.product.ProductDetailActivity
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber

class HomeFragment: NikeFragment() , ProductListAdapter.ProductEventListener {
     val homeViewModel: HomeViewModel by viewModel()
     val latest_product_adapter: ProductListAdapter by inject { parametersOf(VIEW_TYPE_ROUND) }
     val bestSelling_product_adapter: ProductListAdapter by inject{parametersOf(VIEW_TYPE_ROUND)}
     lateinit var bannerSliderViewPager:ViewPager2
     lateinit var sliderIndicator:DotsIndicator
     lateinit var rv_Latest_Products:RecyclerView
     lateinit var rv_BestSelling_Products:RecyclerView
     lateinit var viewLatestProductBtn:MaterialButton


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bannerSliderViewPager=view.findViewById(R.id.bannerSliderViewPager)
        sliderIndicator=view.findViewById(R.id.slider_indicator)
        viewLatestProductBtn=view.findViewById(R.id.viewLatestProductctBtn)

        bestSelling_product_adapter.productEventListener=this
        latest_product_adapter.productEventListener=this


        rv_Latest_Products=view.findViewById(R.id.latestProductsRv)
        rv_Latest_Products.layoutManager=LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,false)
        rv_Latest_Products.adapter=latest_product_adapter

        rv_BestSelling_Products=view.findViewById(R.id.bestSellingProductsRv)
        rv_BestSelling_Products.layoutManager=LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,false)
        rv_BestSelling_Products.adapter=bestSelling_product_adapter

        homeViewModel.prograssLiveData.observe(viewLifecycleOwner){
            setPrograssBarIndicator(it)
        }
        homeViewModel.latest_productLiveData.observe(viewLifecycleOwner){
            Timber.i(it.toString())
            latest_product_adapter.products= it as ArrayList<Product>
        }
        homeViewModel.bestSelling_productLiveData.observe(viewLifecycleOwner){
            Timber.i(it.toString())
            bestSelling_product_adapter.products= it as ArrayList<Product>
        }
        homeViewModel.bannerLiveData.observe(viewLifecycleOwner){
            val bannerSliderAdapter= BannerSliderAdapter(this,it)
            bannerSliderViewPager.adapter=bannerSliderAdapter

            val viewPagerheight=(((bannerSliderViewPager.measuredWidth - convertDpToPx(32f,requireContext()) ) * 173) / 328).toInt()
            val layoutparams=bannerSliderViewPager.layoutParams
            layoutparams.height=viewPagerheight
            bannerSliderViewPager.layoutParams=layoutparams
            sliderIndicator.setViewPager2(bannerSliderViewPager)

        }
        viewLatestProductBtn.setOnClickListener{
            startActivity(Intent(requireContext(),ProductListActivity::class.java).apply {
                putExtra(EXTRA_KEY_ID, SORT_LATEST)
            })
        }




    }

    override fun onProductClick(product: Product) {
        startActivity(Intent(requireContext(),ProductDetailActivity::class.java).apply {
            putExtra(EXTRA_KEY_DATA,product)
        })
    }

    override fun onFavoriteBtnClick(product: Product) {
        homeViewModel.addProductToFavorite(product)
    }
}