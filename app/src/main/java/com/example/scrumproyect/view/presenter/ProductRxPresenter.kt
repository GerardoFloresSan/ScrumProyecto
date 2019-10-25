package com.example.scrumproyect.view.presenter

import com.example.scrumproyect.data.entity.ArticleEntity
import com.example.scrumproyect.domain.usecase.GetProduct
import com.example.scrumproyect.view.presenter.base.BasePresenter
import io.reactivex.observers.DisposableObserver
import java.io.Serializable

class ProductRxPresenter(var getProduct: GetProduct) : BasePresenter<ProductRxPresenter.View>() {

    fun syncProduct() {
        view?.showLoading()
        //TODO gerardo debia migrarlo todo a Clean que no te engañe XD
        getProduct.execute(object : DisposableObserver<ArrayList<ArticleEntity>>() {
            override fun onComplete() {
                view?.hideLoading()
            }

            override fun onNext(value: ArrayList<ArticleEntity>) {
                view?.successSchedule(0, value)
            }

            override fun onError(e: Throwable) {
                view?.hideLoading()
                view?.showError(e.toString())
                view?.successSchedule(1)
            }
        })
    }

    interface View : BasePresenter.View {
        fun successSchedule(flag: Int, vararg args: Serializable)
    }
}