package com.myapp.nikestore.feachers.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.myapp.nikestore.NikeFragment
import com.myapp.nikestore.R
import com.myapp.nikestore.feachers.auth.AuthActivity
import com.myapp.nikestore.feachers.favorites.FavoriteProductsActivity
import com.myapp.nikestore.feachers.order.OrderHistoryActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class ProfileFragment: NikeFragment() {
 val profileViewModel:ProfileViewModel by viewModel()
    lateinit var userNameTv_Profile:TextView
    lateinit var signInOrSignUpBtn_Profile:TextView
    lateinit var favoriteBtn_Profile:TextView
    lateinit var orderHistory_Profile:TextView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userNameTv_Profile=view.findViewById(R.id.userNameTv_Profile)
        signInOrSignUpBtn_Profile=view.findViewById(R.id.signInOrSignUpBtn_Profile)
        favoriteBtn_Profile=view.findViewById(R.id.favoriteBtn_Profile)
        orderHistory_Profile=view.findViewById(R.id.orderHistory_Profile)


        favoriteBtn_Profile.setOnClickListener {
            startActivity(Intent(requireContext(),FavoriteProductsActivity::class.java))
        }

        orderHistory_Profile.setOnClickListener {
            startActivity(Intent(context,OrderHistoryActivity::class.java))

        }


    }

    override fun onResume() {
        super.onResume()
        checkAuthState()
    }

    private fun checkAuthState() {
        if(profileViewModel.isSignedIn){
            userNameTv_Profile.text=profileViewModel.userName
            signInOrSignUpBtn_Profile.text=getString(R.string.signOut)
            signInOrSignUpBtn_Profile.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_sign_out,0)
            signInOrSignUpBtn_Profile.setOnClickListener {
                profileViewModel.signOut()
                checkAuthState()
            }
        }
        else{
            userNameTv_Profile.text=getString(R.string.guestUser)
            signInOrSignUpBtn_Profile.text=getString(R.string.signIn)
            signInOrSignUpBtn_Profile.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_sign_in,0)
            signInOrSignUpBtn_Profile.setOnClickListener {
                startActivity(Intent(requireContext(),AuthActivity::class.java))

            }

        }
    }
}