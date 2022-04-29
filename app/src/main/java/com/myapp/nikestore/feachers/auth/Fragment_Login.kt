package com.myapp.nikestore.feachers.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.myapp.nikestore.NikeFragment
import com.myapp.nikestore.R
import com.myapp.nikestore.common.NikeCompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.koin.androidx.viewmodel.ext.android.viewModel

class Fragment_Login:Fragment() {
    val authViewModel:AuthViewModel by viewModel()
    val compositeDisposable=CompositeDisposable()
    lateinit var emailEt:EditText
    lateinit var passwordEt:EditText
    lateinit var loginBtn:MaterialButton
    lateinit var signupLinkBtn: MaterialButton


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        emailEt=view.findViewById(R.id.emailEt_Login)
        passwordEt=view.findViewById(R.id.passwordEt_Login)
        loginBtn=view.findViewById(R.id.loginBtn)
        signupLinkBtn=view.findViewById(R.id.signUpLinkBtn)

        loginBtn.setOnClickListener {
            authViewModel.loginUser(emailEt.text.toString(),passwordEt.text.toString())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object :NikeCompletableObserver(compositeDisposable){
                    override fun onComplete() {
                       requireActivity().finish()
                    }

                })
        }
        signupLinkBtn.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentcontainer_auth,Fragment_SignUp())
                commit()
            }
        }


    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }
}
