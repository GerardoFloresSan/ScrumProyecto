package com.example.scrumproyect.view.presenter

import com.example.scrumproyect.data.entity.ArticleEntity
import com.example.scrumproyect.data.entity.LikeEntity
import com.example.scrumproyect.view.presenter.base.BasePresenter
import com.example.scrumproyect.view.ui.utils.PapersManager
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.*
import java.io.Serializable

class ArticlePresenter : BasePresenter<ArticlePresenter.View>() {
    private var fireBaseFireStore =  FirebaseFirestore.getInstance()

    fun syncArticles() {
        //TODO BUSCADOR POR QUERY PARA PAGINADO DE 5 EN 5 PARA REDUCIR LA CARGA DEL APP
        val getTask = fireBaseFireStore.collection("articles").orderBy("timeCreate", Query.Direction.DESCENDING).whereEqualTo("status", 0).get()
        val articles = arrayListOf<ArticleEntity>()

        getTask.addOnSuccessListener {
            view.takeIf { view != null }.apply {
                it.forEach { snapshot ->
                    val articleE = snapshot.toObject(ArticleEntity::class.java)
                    articleE.idM = snapshot.id
                    articles.add(articleE)

                }
                view?.successArticle(0, articles)
            }
        }
        getTask.addOnFailureListener(getSimpleFailureListener())
    }

    fun removeArticle(idArticle : String) {
        view?.showLoading()
        val removeTask = fireBaseFireStore.collection("articles").document(idArticle).update(
            "status", 1
        )

        removeTask.addOnSuccessListener {
            view.takeIf { view != null }.apply {
                view?.hideLoading()
                view?.successArticle(2)
            }
        }

        removeTask.addOnFailureListener(getSimpleFailureListener())
    }

    fun removeLike(type : Int, idArticle : String) {
        view?.showLoading()
        val addLike : Task<Void> =
            when(type) {
            0 ->  {
                 fireBaseFireStore.collection("articles").document(idArticle).update(
                    "sad", FieldValue.arrayUnion(PapersManager.userEntity.uidUser),
                    "neutral", FieldValue.arrayRemove(PapersManager.userEntity.uidUser),
                    "happy", FieldValue.arrayRemove(PapersManager.userEntity.uidUser)
                )

            }
            1 ->  {
                fireBaseFireStore.collection("articles").document(idArticle).update(
                    "neutral", FieldValue.arrayUnion(PapersManager.userEntity.uidUser),
                    "sad", FieldValue.arrayRemove(PapersManager.userEntity.uidUser),
                    "happy", FieldValue.arrayRemove(PapersManager.userEntity.uidUser)
                )
            }
            2 ->  {
                fireBaseFireStore.collection("articles").document(idArticle).update(
                    "happy", FieldValue.arrayUnion(PapersManager.userEntity.uidUser),
                    "sad", FieldValue.arrayRemove(PapersManager.userEntity.uidUser),
                    "neutral", FieldValue.arrayRemove(PapersManager.userEntity.uidUser)
                )
            }
            else ->
                fireBaseFireStore.collection("articles").document(idArticle).update(
                    "happy", FieldValue.arrayRemove(PapersManager.userEntity.uidUser),
                    "sad", FieldValue.arrayRemove(PapersManager.userEntity.uidUser),
                    "neutral", FieldValue.arrayRemove(PapersManager.userEntity.uidUser)
                )
            }

        addLike.addOnSuccessListener {
            view.takeIf { view != null }.apply {
                view?.hideLoading()
                view?.successArticle(1, type)
            }
        }

        addLike.addOnFailureListener(getSimpleFailureListener())
    }

    fun syncMeArticles() {
        val getTask = fireBaseFireStore.collection("articles").orderBy("timeCreate", Query.Direction.DESCENDING).whereEqualTo("idUser", PapersManager.userEntity.uidUser).whereEqualTo("status", 0).get()
        val articles = arrayListOf<ArticleEntity>()

        getTask.addOnSuccessListener {
            view.takeIf { view != null }.apply {
                it.forEach { snapshot ->
                    val articleE = snapshot.toObject(ArticleEntity::class.java)
                    articleE.idM = snapshot.id
                    articles.add(articleE)

                }
                view?.successArticle(0, articles)
            }
        }
        getTask.addOnFailureListener(getSimpleFailureListener())
    }

    fun addArticle(article: ArticleEntity, type: Int) {
        view?.showLoading()
        val key = fireBaseFireStore.collection("articles").document()

        article.apply {
            idM = key.id
            idUser = PapersManager.userEntity.uidUser
        }

        val like = LikeEntity().apply {
            this.idUser = PapersManager.userEntity.uidUser
            this.type = type
        }

        val refTask = fireBaseFireStore.collection("articles").document(key.id).set(article)
        val setLike = fireBaseFireStore.collection("articles").document(key.id).collection("likes").document(PapersManager.userEntity.uidUser).set(like)

        val allTask = Tasks.whenAll(refTask, setLike)
        allTask.addOnSuccessListener {
            view.takeIf { view != null }.apply {
                view?.hideLoading()
                view?.successArticle(1)
            }
        }
        allTask.addOnFailureListener(getSimpleFailureListener())
    }

    private fun addLike(id : String, type: Int) {
        val like = LikeEntity().apply {
            this.idUser = PapersManager.userEntity.uidUser
            this.type = type
        }

        val setLike = fireBaseFireStore.collection("articles").document(id).collection("likes").document(PapersManager.userEntity.uidUser).set(like)

        setLike.addOnSuccessListener {
            view.takeIf { view != null }.apply {
                view?.successArticle(1)
            }
        }

        setLike.addOnFailureListener(getSimpleFailureListener())
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