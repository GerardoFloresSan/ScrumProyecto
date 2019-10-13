package com.example.scrumproyect.view.presenter.base

import android.content.Context
import androidx.annotation.IntegerRes

/**

 * @autor @RaúlEspinoza on 25/06/2018.

 */
open class BasePresenter<V : BasePresenter.View> {

    protected var view: V? = null

    fun attachView(view: V) {
        this.view = view
    }

    fun detachView() {
        this.view = null
    }

    interface View {

        fun getContext(): Context

        fun showLoading()

        fun hideLoading()

        fun showError(message: String)

        fun showError(@IntegerRes message: Int)
    }
}