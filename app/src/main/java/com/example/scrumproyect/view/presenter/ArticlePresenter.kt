package com.example.scrumproyect.view.presenter

import com.example.scrumproyect.data.entity.ArticleEntity
import com.example.scrumproyect.data.entity.LikeEntity
import com.example.scrumproyect.view.presenter.base.BasePresenter
import com.example.scrumproyect.view.ui.utils.PapersManager
import com.example.scrumproyect.view.ui.utils.linkpewview.MetaDataKotlin
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.*
import java.io.Serializable

class ArticlePresenter : BasePresenter<ArticlePresenter.View>() {
    private var fireBaseFireStore =  FirebaseFirestore.getInstance()

    fun syncArticles() {
        val getTask = fireBaseFireStore.collection("articles").orderBy("timeCreate", Query.Direction.DESCENDING).whereEqualTo("status", 0).get()
        val articles = arrayListOf<ArticleEntity>()

        getTask.addOnSuccessListener {
            view.takeIf { view != null }.apply {
                it.forEach { snapshot ->
                    val articleE = snapshot.toObject(ArticleEntity::class.java)
                    articleE.idM = snapshot.id
                    articles.add(articleE)
                }
                val sortedList = articles.sortedByDescending { article -> article.sad.size; article.neutral.size; article.happy.size }
                val articles2 = arrayListOf<ArticleEntity>()
                articles2.addAll(sortedList)
                view?.successArticle(0, articles2)
            }
        }
        getTask.addOnFailureListener(getSimpleFailureListener())
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
                val sortedList = articles.sortedByDescending { article -> article.sad.size; article.neutral.size; article.happy.size }
                val articles2 = arrayListOf<ArticleEntity>()
                articles2.addAll(sortedList)
                view?.successArticle(0, articles2)
            }
        }
        getTask.addOnFailureListener(getSimpleFailureListener())
    }

    fun existUrl(article: ArticleEntity, type: Int) : Boolean{
        view?.showLoading()
        val getTask = fireBaseFireStore.collection("articles").orderBy("timeCreate", Query.Direction.DESCENDING).whereEqualTo("urlM", article.urlM).whereEqualTo("status", 0).get()
        val articles = arrayListOf<ArticleEntity>()

        getTask.addOnSuccessListener{
            view.takeIf { view != null }.apply {
                view?.hideLoading()
                it.forEach { snapshot ->
                    val articleE = snapshot.toObject(ArticleEntity::class.java)
                    articleE.idM = snapshot.id
                    articles.add(articleE)
                }
                val flag = if (articles.isEmpty()) 10 else 11
                val articleTemp = if (articles.isEmpty()) article else articles.first()

                view?.successArticle(flag, articleTemp, type)
            }
        }

        getTask.addOnFailureListener(getSimpleFailureListener())

        return true
    }

    fun removeArticle(idArticle : String) {
        view?.showLoading()
        val removeTask = fireBaseFireStore.collection("articles").document(idArticle).update(
            "status", 1
        )
        val removeUrl = fireBaseFireStore.collection("urls").document(idArticle).delete()

        val allTask = Tasks.whenAll(removeTask, removeUrl)

        allTask.addOnSuccessListener {
            view.takeIf { view != null }.apply {
                view?.hideLoading()
                view?.successArticle(2)
            }
        }

        allTask.addOnFailureListener(getSimpleFailureListener())
    }

    fun addUpdateLike(type : Int, idArticle : String) {
        view?.showLoading()
        val addLike : Task<Void> =
            when(type) {
            0 ->  {
                 fireBaseFireStore.collection("articles").document(idArticle).update(
                     "sad", FieldValue.arrayRemove(PapersManager.userEntity.uidUser),
                     "neutral", FieldValue.arrayRemove(PapersManager.userEntity.uidUser),
                    "happy", FieldValue.arrayRemove(PapersManager.userEntity.uidUser),

                         "sad", FieldValue.arrayUnion(PapersManager.userEntity.uidUser)
                )

            }
            1 ->  {
                fireBaseFireStore.collection("articles").document(idArticle).update(
                    "sad", FieldValue.arrayRemove(PapersManager.userEntity.uidUser),
                    "happy", FieldValue.arrayRemove(PapersManager.userEntity.uidUser),
                        "neutral", FieldValue.arrayRemove(PapersManager.userEntity.uidUser),

                        "neutral", FieldValue.arrayUnion(PapersManager.userEntity.uidUser)
                )
            }
            2 ->  {
                fireBaseFireStore.collection("articles").document(idArticle).update(
                    "happy", FieldValue.arrayRemove(PapersManager.userEntity.uidUser),
                    "sad", FieldValue.arrayRemove(PapersManager.userEntity.uidUser),
                    "neutral", FieldValue.arrayRemove(PapersManager.userEntity.uidUser),

                        "happy", FieldValue.arrayUnion(PapersManager.userEntity.uidUser)
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
                view?.successArticle(1, type, idArticle)
            }
        }

        addLike.addOnFailureListener(getSimpleFailureListener())
    }

    fun addUpdateLikeTwo(type : Int, article : ArticleEntity) {
        view?.showLoading()
        val addLike : Task<Void> =
            when(type) {
                0 ->  {
                    fireBaseFireStore.collection("articles").document(article.idM).update(
                        "sad", FieldValue.arrayRemove(PapersManager.userEntity.uidUser),
                        "neutral", FieldValue.arrayRemove(PapersManager.userEntity.uidUser),
                        "happy", FieldValue.arrayRemove(PapersManager.userEntity.uidUser),

                        "sad", FieldValue.arrayUnion(PapersManager.userEntity.uidUser)
                    )

                }
                1 ->  {
                    fireBaseFireStore.collection("articles").document(article.idM).update(
                        "sad", FieldValue.arrayRemove(PapersManager.userEntity.uidUser),
                        "happy", FieldValue.arrayRemove(PapersManager.userEntity.uidUser),
                        "neutral", FieldValue.arrayRemove(PapersManager.userEntity.uidUser),

                        "neutral", FieldValue.arrayUnion(PapersManager.userEntity.uidUser)
                    )
                }
                2 ->  {
                    fireBaseFireStore.collection("articles").document(article.idM).update(
                        "happy", FieldValue.arrayRemove(PapersManager.userEntity.uidUser),
                        "sad", FieldValue.arrayRemove(PapersManager.userEntity.uidUser),
                        "neutral", FieldValue.arrayRemove(PapersManager.userEntity.uidUser),

                        "happy", FieldValue.arrayUnion(PapersManager.userEntity.uidUser)
                    )
                }
                else ->
                    fireBaseFireStore.collection("articles").document(article.idM).update(
                        "happy", FieldValue.arrayRemove(PapersManager.userEntity.uidUser),
                        "sad", FieldValue.arrayRemove(PapersManager.userEntity.uidUser),
                        "neutral", FieldValue.arrayRemove(PapersManager.userEntity.uidUser)
                    )
            }

        addLike.addOnSuccessListener {
            view.takeIf { view != null }.apply {
                view?.hideLoading()
                view?.successArticle(13, article, type)
            }
        }

        addLike.addOnFailureListener(getSimpleFailureListener())
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
                view?.successArticle(2, article)
            }
        }
        allTask.addOnFailureListener(getSimpleFailureListener())
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
                        else -> view?.showError("Vuelva a intentarlo más tarde")
                        /*it.message!!*/
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