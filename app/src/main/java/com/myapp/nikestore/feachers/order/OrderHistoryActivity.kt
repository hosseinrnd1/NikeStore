package com.myapp.nikestore.feachers.order

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myapp.nikestore.NikeActivity
import com.myapp.nikestore.R
import com.myapp.nikestore.view.NikeToolbar
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel

class OrderHistoryActivity : NikeActivity() {
    val orderHistoryViewModel:OrderHistoryViewModel by viewModel()
    lateinit var orderHistoryRv:RecyclerView
    lateinit var orderHistoryAdapter: OrderHistoryAdapter
    lateinit var orderHistoryToolbar:NikeToolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_history)
        orderHistoryRv=findViewById(R.id.orderHistoryRv)
        orderHistoryToolbar=findViewById(R.id.orderHistoryToolbar)

        orderHistoryViewModel.prograssLiveData.observe(this){
            setPrograssBarIndicator(it)
        }
        orderHistoryRv.layoutManager=LinearLayoutManager(this,RecyclerView.VERTICAL,false)

        orderHistoryViewModel.orders.observe(this){
            orderHistoryAdapter= OrderHistoryAdapter(this,get(),it)
            orderHistoryRv.adapter=orderHistoryAdapter
        }

        orderHistoryToolbar.onBackButtonClickListener=View.OnClickListener {
            finish()
        }
    }
}