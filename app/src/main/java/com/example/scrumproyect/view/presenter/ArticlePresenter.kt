package com.example.scrumproyect.view.presenter

import com.example.scrumproyect.data.entity.ArticleEntity
import com.example.scrumproyect.view.presenter.base.BasePresenter
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestore
import java.io.Serializable

class ArticlePresenter : BasePresenter<ArticlePresenter.View>() {
    private var fireBaseFireStore =  FirebaseFirestore.getInstance()

    fun syncArticles() {
        view?.showLoading()

        val getTask = fireBaseFireStore.collection("articles").get()
        val articles = arrayListOf<ArticleEntity>()

        getTask.addOnSuccessListener {
            view.takeIf { view != null }.apply {
                view?.hideLoading()
                it.forEach { snapshot ->
                    val articletE = snapshot.toObject(ArticleEntity::class.java)
                    articletE.idM = snapshot.id
                    articles.add(articletE)

                }
                view?.successArticle(0, articles)
            }
        }
        getTask.addOnFailureListener(getSimpleFailureListener())
    }

    fun addArticle(article: ArticleEntity) {
        view?.showLoading()
        val refTask = fireBaseFireStore.collection("articles")
            .add(article)

        refTask.addOnSuccessListener {
            view.takeIf { view != null }.apply {
                view?.hideLoading()
                view?.successArticle(1)
            }
        }
        refTask.addOnFailureListener(getSimpleFailureListener())
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
        fun successArticle(flag: Int, vararg args: Serializable)
    }
}