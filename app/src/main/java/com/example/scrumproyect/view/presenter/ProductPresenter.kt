package com.example.scrumproyect.view.presenter

import com.example.scrumproyect.data.entity.ProductEntity
import com.example.scrumproyect.view.presenter.base.BasePresenter
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestore
import java.io.Serializable

class ProductPresenter : BasePresenter<ProductPresenter.View>() {
    private var fireBaseFireStore =  FirebaseFirestore.getInstance()

    fun syncProduct() {
        view?.showLoading()

        val getTask = fireBaseFireStore.collection("products").get()
        val products = arrayListOf<ProductEntity>()

        getTask.addOnSuccessListener {
            view.takeIf { view != null }.apply {
                view?.hideLoading()
                it.forEach { snapshot ->
                    val productE = snapshot.toObject(ProductEntity::class.java)
                    productE.id = snapshot.id
                    products.add(productE)

                }
                view?.successSchedule(0, products)
            }
        }
        getTask.addOnFailureListener(getSimpleFailureListener())
    }

    fun addProduct(product: ProductEntity) {
        view?.showLoading()
        val refTask = fireBaseFireStore.collection("products")
            .add(product)

        refTask.addOnSuccessListener {
            view.takeIf { view != null }.apply {
                view?.hideLoading()
                view?.successSchedule(1)
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