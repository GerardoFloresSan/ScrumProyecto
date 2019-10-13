package com.example.scrumproyect.view.presenter

import com.example.scrumproyect.domain.usecase.GetProduct
import com.example.scrumproyect.view.presenter.base.BasePresenter
import com.example.scrumproyect.view.viewModel.ProductViewModel
import io.reactivex.observers.DisposableObserver
import java.io.Serializable

class ProductRxPresenter(var getProduct: GetProduct) : BasePresenter<ProductRxPresenter.View>() {

    fun syncSchedule() {
        view?.showLoading()

        getProduct.execute(object : DisposableObserver<ArrayList<ProductViewModel>>() {
            override fun onComplete() {
                view?.hideLoading()
            }

            override fun onNext(value: ArrayList<ProductViewModel>) {
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