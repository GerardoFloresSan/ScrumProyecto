package com.example.scrumproyect.di.module

import com.example.scrumproyect.data.repository.ProductRepository
import com.example.scrumproyect.domain.usecase.GetProduct
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Named
import javax.inject.Singleton

/**

 * @autor @RaúlEspinoza on 9/07/2018.

 */
@Module
class UsesCaseModule {

    @Provides
    @Singleton
    fun getProduct(
        productRepository: ProductRepository,
        @Named("executor_thread") executorThread: Scheduler,
        @Named("ui_thread") uiThread: Scheduler
    ) = GetProduct(executorThread, uiThread, productRepository)

    @Provides
    @Named("executor_thread")
    fun provideExecutorThread(): Scheduler = Schedulers.io()

    @Provides
    @Named("ui_thread")
    fun provideUiThread(): Scheduler = AndroidSchedulers.mainThread()

}