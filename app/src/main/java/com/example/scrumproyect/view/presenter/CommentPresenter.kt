package com.example.scrumproyect.view.presenter

import com.example.scrumproyect.data.entity.CommentEntity
import com.example.scrumproyect.view.presenter.base.BasePresenter
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.io.Serializable

class CommentPresenter : BasePresenter<CommentPresenter.View>() {
    private var fireBaseFireStore =  FirebaseFirestore.getInstance()

    fun syncComment(id: String) {
        view?.showLoading()

        val getTask = fireBaseFireStore.collection("articles").document(id).collection("comments").orderBy("time", Query.Direction.DESCENDING).get()
        val comments = arrayListOf<CommentEntity>()

        getTask.addOnSuccessListener {
            view.takeIf { view != null }.apply {
                view?.hideLoading()
                it.forEach { snapshot ->
                    val commentE = snapshot.toObject(CommentEntity::class.java)
                    commentE.id = snapshot.id
                    comments.add(commentE)

                }
                view?.successSchedule(0, comments)
            }
        }
        getTask.addOnFailureListener(getSimpleFailureListener())
    }

    fun addComment(id: String, comment: CommentEntity) {
        view?.showLoading()
        val refTask = fireBaseFireStore.collection("articles").document(id).collection("comments").add(comment)

        refTask.addOnSuccessListener {
            view.takeIf { view != null }.apply {
                view?.hideLoading()
                view?.successSchedule(1, comment)
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
        fun successSchedule(flag: Int, vararg args: Serializable)
    }
}