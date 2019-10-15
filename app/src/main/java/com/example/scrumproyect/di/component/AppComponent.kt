package com.example.scrumproyect.di.component

import android.content.Context
import dagger.Component
import com.example.scrumproyect.di.module.AppModule
import com.example.scrumproyect.di.module.FirebaseModule
import com.example.scrumproyect.di.module.RepositoryModule
import com.example.scrumproyect.di.module.UsesCaseModule
import com.example.scrumproyect.domain.usecase.GetProduct
import javax.inject.Singleton



/**

 * @autor @RaúlEspinoza on 25/06/2018.

 */
@Singleton
@Component(modules = [AppModule::class, UsesCaseModule::class, RepositoryModule::class, FirebaseModule::class])
interface AppComponent {

    fun context(): Context

    fun getProductUseCase(): GetProduct
}
/*@Singleton
@Component(modules = [AppModule::class], dependencies = [UsesCaseComponent::class])
interface AppComponent {

    fun context(): Context

}*/

/*@Component(modules = [FirebaseModule::class], dependencies = [AppComponent::class])
interface FirebaseComponent

@Component(modules = [UsesCaseModule::class], dependencies = [FirebaseComponent::class])
interface UsesCaseComponent

@Component(modules = [RepositoryModule::class], dependencies = [UsesCaseComponent::class])
interface RepositoryComponent*/

