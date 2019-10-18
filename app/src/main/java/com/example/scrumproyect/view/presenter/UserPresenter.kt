package com.example.scrumproyect.view.presenter

import com.example.scrumproyect.BuildConfig
import com.example.scrumproyect.data.entity.UserEntity
import com.example.scrumproyect.view.presenter.base.BasePresenter
import com.example.scrumproyect.view.ui.utils.PapersManager
import com.facebook.AccessToken
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.*
import java.io.Serializable

class UserPresenter : BasePresenter<UserPresenter.View>() {
    private var fireBaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var mGoogleSignInOptions: GoogleSignInOptions

    fun login(emailN : String, passwordN : String) {
        view?.showLoading()

        val loginTask = FirebaseAuth.getInstance().signInWithEmailAndPassword(emailN, passwordN)
        loginTask.addOnSuccessListener {
            view.takeIf { view != null }.apply {
                view?.hideLoading()
                it.user?.let { it1 ->
                    PapersManager.userEntity = UserEntity().apply {
                        id = it1.uid
                        email = it1.email!!
                        type = 0
                    }
                    PapersManager.session = true
                    view?.successUser(0)
                }
            }
        }
        loginTask.addOnFailureListener(getSimpleFailureListener())
    }

    fun loginFaceBook(accessToken: AccessToken) {
        view?.showLoading()

        val credential = FacebookAuthProvider.getCredential(accessToken.token)
        val authTask = fireBaseAuth.signInWithCredential(credential)
        authTask.addOnSuccessListener {
            view.takeIf { view != null }.apply {
                view?.hideLoading()
                it.user?.let { it1 ->
                    PapersManager.userEntity = UserEntity().apply {
                        id = it1.uid
                        email = if(it1.email != null) it1.email!! else it1.providerData[1].email!!
                        type = 1
                    }
                    PapersManager.session = true
                    view?.successUser(1)
                }
            }
        }

        authTask.addOnFailureListener(getSimpleFailureListener())
    }

    fun loginGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        val authTask = fireBaseAuth.signInWithCredential(credential)

        authTask.addOnSuccessListener {
            view.takeIf { view != null }.apply {
                view?.hideLoading()
                it.user?.let { it1 ->
                    PapersManager.userEntity = UserEntity().apply {
                        id = it1.uid
                        email = it1.email!!
                        type = 2
                    }
                    PapersManager.session = true
                    view?.successUser(2)
                }
            }
        }
        authTask.addOnFailureListener(getSimpleFailureListener())
    }

    fun newUser(emailN : String, passwordN : String) {
        view?.showLoading()

        val newUser = fireBaseAuth.createUserWithEmailAndPassword(emailN, passwordN)
        newUser.addOnSuccessListener {
            view.takeIf { view != null }.apply {
                view?.hideLoading()
                PapersManager.userEntity = UserEntity().apply {
                    id = it.user!!.uid
                    email = emailN
                }
                view?.successUser(0)
            }
        }
        newUser.addOnFailureListener(getSimpleFailureListener())
    }

    fun logout(tokenG: String) {
        view?.showLoading()
        when(PapersManager.userEntity.type) {
            0 -> outFireBase(3)
            1 -> outFaceBook(4)
            2 -> outGoogle(tokenG, 5)
        }
    }

    private fun outFireBase(type : Int) {
        view?.hideLoading()
        PapersManager.userEntity = UserEntity()
        PapersManager.session = false
        fireBaseAuth.signOut()
        view?.hideLoading()
        view?.successUser(type)
    }

    private fun outFaceBook(type : Int) {
        LoginManager.getInstance().logOut()
        outFireBase(type)
    }

    private fun outGoogle(tokenG: String, type : Int) {
        mGoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(tokenG)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(view!!.getContext(), mGoogleSignInOptions)
        val singOut = mGoogleSignInClient.signOut()
        singOut.addOnSuccessListener {
            fireBaseAuth.signOut()
            view.takeIf { view != null }.apply {
                outFireBase(type)
            }
        }

        singOut.addOnFailureListener(getSimpleFailureListener())
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
        fun successUser(flag: Int, vararg args: Serializable)
    }
}