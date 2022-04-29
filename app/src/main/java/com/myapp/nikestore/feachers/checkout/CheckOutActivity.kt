package com.myapp.nikestore.feachers.checkout

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.myapp.nikestore.R
import com.myapp.nikestore.common.EXTRA_KEY_DATA
import com.myapp.nikestore.common.formatPrice
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class CheckOutActivity : AppCompatActivity() {
    lateinit var purchaseStatusTv:TextView
    lateinit var orderStatusTv_CheckOut:TextView
    lateinit var purchasePrice_CheckOut:TextView

     val checkOutViewModel:CheckOutViewModel by viewModel{
         val uri: Uri?=intent.data
         if(uri!=null){
             //online
             parametersOf(uri.getQueryParameter("order_id")!!.toInt())
         }
         else{
             parametersOf(intent.extras!!.getInt(EXTRA_KEY_DATA))
         }
     }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_out)

        purchaseStatusTv=findViewById(R.id.purchaseStatusTv)
        orderStatusTv_CheckOut=findViewById(R.id.orderStatusTv_CheckOut)
        purchasePrice_CheckOut=findViewById(R.id.purchasePrice_CheckOut)



        checkOutViewModel.checkOutLivedata.observe(this){
            purchaseStatusTv.text=if(it.purchase_success) "خرید با موفقیت انجام شد" else "خرید ناموفق"
            orderStatusTv_CheckOut.text=it.payment_status
            purchasePrice_CheckOut.text= formatPrice(it.payable_price)
        }
    }
}