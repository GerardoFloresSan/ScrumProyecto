package com.example.scrumproyect.view.presenter

import android.util.Log
import com.example.scrumproyect.BuildConfig
import com.example.scrumproyect.data.entity.CommentEntity
import com.example.scrumproyect.data.entity.UserEntity
import com.example.scrumproyect.view.presenter.base.BasePresenter
import com.facebook.AccessToken
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import io.paperdb.Paper
import java.io.Serializable

class UserPresenter : BasePresenter<UserPresenter.View>() {
    private var fireBaseFireStore =  FirebaseFirestore.getInstance()

    fun login() {
        view?.showLoading()

        val loginTask = FirebaseAuth.getInstance().signInWithEmailAndPassword("alonsopantigoso91@gmail.com", "qwerty1234")
        loginTask.addOnSuccessListener {
            view.takeIf { view != null }.apply {
                view?.hideLoading()
                it.user?.let { it1 ->
                    Paper.book(BuildConfig.FLAVOR).write("user", UserEntity().apply {
                        id = it1.uid
                        email = it1.email!!
                    })
                    Log.d("tag-user", Paper.book(BuildConfig.FLAVOR).read("user", UserEntity()).id)
                    Log.d("tag-user", Paper.book(BuildConfig.FLAVOR).read("user", UserEntity()).email)
                    view?.successSchedule(0)
                }
            }
        }
        loginTask.addOnFailureListener(getSimpleFailureListener())
    }

    fun loginFaceBook(accessToken: AccessToken) {
        view?.showLoading()

        val credential = FacebookAuthProvider.getCredential(accessToken.token)
        val authTask = FirebaseAuth.getInstance().signInWithCredential(credential)
        authTask.addOnSuccessListener {
            view.takeIf { view != null }.apply {
                view?.hideLoading()
                it.user?.let { it1 ->
                    Paper.book(BuildConfig.FLAVOR).write("user", UserEntity().apply {
                        id = it1.uid
                        email = it1.email!!
                    })
                    Log.d("tag-user", Paper.book(BuildConfig.FLAVOR).read("user", UserEntity()).id)
                    Log.d("tag-user", Paper.book(BuildConfig.FLAVOR).read("user", UserEntity()).email)
                    view?.successSchedule(0)
                }
            }
        }

        authTask.addOnFailureListener(getSimpleFailureListener())
    }

    fun getUser(user: FirebaseUser) {
        val userRef = fireBaseFireStore.collection("user").get()

        userRef.addOnSuccessListener {
            view.takeIf { view != null }.apply {
                view?.hideLoading()
                Paper.book(BuildConfig.FLAVOR).write("user", UserEntity().apply {
                    id = user.uid
                    email = user.email!!
                })
                view?.successSchedule(0)
            }
        }
        userRef.addOnFailureListener(getSimpleFailureListener())
    }

    private fun getSimpleFailureListener(): OnFailureListener {
        return OnFailureListener {
            view.takeIf { view != null }.apply {
                view?.hideLoading()

                if (it is FirebaseAuthException) {
                    when (it.errorCode) {
                        "ERROR_USER_NOT_FOUND" -> view?.showError("El usuario no existe")
                        "ERROR_WRONG_PASSWORD" -> view?.showError("Contraseña inválida")
                        "ERROR_WEAK_PASSWORD" -> view?.showError("La contraseña debe tener mínimo 6 caracteres")
                        "ERROR_INVALID_EMAIL" -> view?.showError("Correo inválido")
                        "ERROR_EMAIL_ALREADY_IN_USE" -> view?.showError("Correo ya registrado")
                        else -> view?.showError(it.message!!)
                    }
                } else {
                    view?.showError(it.message!!)
                }
            }
        }
    }

    interface View : BasePresenter.View {
        fun successSchedule(flag: Int, vararg args: Serializable)
    }
}