package com.myapp.nikestore.feachers.order

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.myapp.nikestore.R
import com.myapp.nikestore.common.convertDpToPx
import com.myapp.nikestore.common.formatPrice
import com.myapp.nikestore.data.Product
import com.myapp.nikestore.services.ImageLoadingService
import com.myapp.nikestore.view.NikeImageView
import com.sevenlearn.nikestore.data.OrderHistoryItem

class OrderHistoryAdapter(val context:Context,val imageLoadingService: ImageLoadingService,val orders:List<OrderHistoryItem>): RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryViewHolder>() {
     val layoutParam:LinearLayout.LayoutParams


    init {
        val size= convertDpToPx(100f,context).toInt()
        val margin= convertDpToPx(8f,context).toInt()
        layoutParam=LinearLayout.LayoutParams(size,size)
        layoutParam.setMargins(margin,0,margin,0)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHistoryViewHolder {
        return OrderHistoryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_order_history,parent,false))
    }

    override fun onBindViewHolder(holder: OrderHistoryViewHolder, position: Int) {
        holder.bind(orders[position])
    }

    override fun getItemCount(): Int {
        return orders.size
    }

    inner class OrderHistoryViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        val orderID_OrderHistory:TextView=itemView.findViewById(R.id.orderID_OrderHistory)
        val orderPrice_OrderHistory:TextView=itemView.findViewById(R.id.orderPrice_OrderHistory)
        val orderHistoryProductLL:LinearLayout=itemView.findViewById(R.id.orderHistoryProductLL)

        fun bind(orderHistoryItem: OrderHistoryItem){
            orderID_OrderHistory.text=orderHistoryItem.id.toString()
            orderPrice_OrderHistory.text= formatPrice(orderHistoryItem.payable)
            orderHistoryProductLL.removeAllViews()

            orderHistoryItem.order_items.forEach {
                val imageView=NikeImageView(context)
                imageView.layoutParams=layoutParam
                imageLoadingService.load(imageView,it.product.image)

                orderHistoryProductLL.addView(imageView)

            }
        }
    }


}