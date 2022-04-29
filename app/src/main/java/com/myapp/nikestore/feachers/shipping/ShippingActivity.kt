package com.myapp.nikestore.feachers.shipping

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.google.android.material.button.MaterialButton
import com.myapp.nikestore.R
import com.myapp.nikestore.common.EXTRA_KEY_DATA
import com.myapp.nikestore.common.NikeSingleObserver
import com.myapp.nikestore.common.asyncNetworkRequest
import com.myapp.nikestore.common.openUrlInCustomTab
import com.myapp.nikestore.data.SubmitOrderResponse
import com.myapp.nikestore.feachers.cart.CartItemAdapter
import com.myapp.nikestore.feachers.checkout.CheckOutActivity
import com.sevenlearn.nikestore.data.PurchaseDetail
import io.reactivex.SingleObserver
import io.reactivex.disposables.CompositeDisposable
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.IllegalStateException


class ShippingActivity : AppCompatActivity() {
    val shippingViewModel:ShippingViewModel by viewModel()
    lateinit var firstNameEt:EditText
    lateinit var lastNameEt:EditText
    lateinit var postalCodeEt:EditText
    lateinit var phoneNumberEt:EditText
    lateinit var addressEt:EditText
    lateinit var onlinePaymentBtn:MaterialButton
    lateinit var codBtn:MaterialButton

    val compositeDisposable=CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shipping)

        firstNameEt=findViewById(R.id.firstNameEt_Shipping)
        lastNameEt=findViewById(R.id.lastNameEt_Shipping)
        postalCodeEt=findViewById(R.id.postalCodeEt_Shipping)
        phoneNumberEt=findViewById(R.id.phoneNumberEt_Shipping)
        addressEt=findViewById(R.id.addressEt_Shipping)
        onlinePaymentBtn=findViewById(R.id.onlinePaymentBtn)
        codBtn=findViewById(R.id.codBtn)


        val purchaseDetail=intent.getParcelableExtra<PurchaseDetail>(EXTRA_KEY_DATA)
            ?:throw IllegalStateException("purchase detail cannot be null")
        val viewHolder=CartItemAdapter.PurchaseDetailViewHolder(findViewById(R.id.purchase_detail_shipping))
        viewHolder.bindPurchaseDetail(purchaseDetail.total_price,purchaseDetail.shipping_cost,purchaseDetail.payable_price)

       val onClickListener= View.OnClickListener {
           shippingViewModel.submit(
               firstNameEt.text.toString(),
               lastNameEt.text.toString(),
               postalCodeEt.text.toString(),
               phoneNumberEt.text.toString(),
               addressEt.text.toString(),
               if(it.id==R.id.onlinePaymentBtn) PAYMENT_METHOD_ONLINE else PAYMENT_METHOD_COD
           ).asyncNetworkRequest()
               .subscribe(object:NikeSingleObserver<SubmitOrderResponse>(compositeDisposable){
                   override fun onSuccess(t: SubmitOrderResponse) {
                       if(t.bank_gateway_url.isNotEmpty()){
                           //online
                           openUrlInCustomTab(this@ShippingActivity,t.bank_gateway_url)


                       }
                       else{
                           startActivity(Intent(this@ShippingActivity,CheckOutActivity::class.java).apply {
                               putExtra(EXTRA_KEY_DATA,t.order_id)
                           })
                       }
                       finish()
                   }

               })
       }
        onlinePaymentBtn.setOnClickListener(onClickListener)
        codBtn.setOnClickListener(onClickListener)


    }
}