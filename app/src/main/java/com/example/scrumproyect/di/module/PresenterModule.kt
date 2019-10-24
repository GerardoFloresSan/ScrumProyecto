package com.example.scrumproyect.di.module

import com.example.scrumproyect.di.PresenterScope
import com.example.scrumproyect.domain.usecase.GetProduct
import com.example.scrumproyect.view.presenter.ProductRxPresenter
import dagger.Module
import dagger.Provides

@Module
@PresenterScope
class PresenterModule {

    @Provides
    @PresenterScope
    fun productRxPresenter(getProduct: GetProduct) = ProductRxPresenter(getProduct)

}