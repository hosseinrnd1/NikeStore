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
import org.koin.android.ext.android.inject

class Fragment_SignUp:Fragment() {
    val authViewModel:AuthViewModel by inject()
    val compositeDisposable=CompositeDisposable()
    lateinit var emailEt: EditText
    lateinit var passwordEt: EditText
    lateinit var signUpBtn: MaterialButton
    lateinit var loginLinkBtn: MaterialButton
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_signup,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        emailEt=view.findViewById(R.id.emailEt_SignUp)
        passwordEt=view.findViewById(R.id.passwordEt_SignUp)
        signUpBtn=view.findViewById(R.id.signUpBtn)
        loginLinkBtn=view.findViewById(R.id.loginLinkBtn)

        signUpBtn.setOnClickListener {
            authViewModel.signUpUser(emailEt.text.toString(),passwordEt.text.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object:NikeCompletableObserver(compositeDisposable){
                    override fun onComplete() {
                        requireActivity().finish()
                    }

                })
        }
        loginLinkBtn.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentcontainer_auth,Fragment_Login())
                commit()
            }
        }

    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }
}