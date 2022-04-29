package com.myapp.nikestore.feachers.main

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.color.MaterialColors
import com.myapp.nikestore.NikeActivity
import com.myapp.nikestore.R
import com.myapp.nikestore.common.convertDpToPx
import com.sevenlearn.nikestore.data.CartItemCount
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : NikeActivity() {
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var bottomNavigationView:BottomNavigationView
    val mainViewModel:MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_container) as NavHostFragment
        navController = navHostFragment.navController

        // Setup the bottom navigation view with navController
        bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationMain)
        bottomNavigationView.setupWithNavController(navController)

        // Setup the ActionBar with navController and 3 top level destinations
        appBarConfiguration = AppBarConfiguration(
            setOf(R.navigation.home, R.navigation.cart,  R.navigation.profile)
        )



    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onCartItemsCountChangeEvent(cartItemCount: CartItemCount){
        val badge=bottomNavigationView.getOrCreateBadge(R.id.cart)
        badge.backgroundColor=MaterialColors.getColor(bottomNavigationView,R.attr.colorPrimary)
        badge.badgeGravity=BadgeDrawable.BOTTOM_END
        badge.number=cartItemCount.count
        badge.verticalOffset= convertDpToPx(10f,this).toInt()
        badge.isVisible=cartItemCount.count>0
    }



    override fun onResume() {
        super.onResume()
        mainViewModel.getCartItemsCount()
    }
}