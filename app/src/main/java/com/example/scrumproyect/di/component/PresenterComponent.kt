package com.example.scrumproyect.di.component

import com.example.scrumproyect.MainActivity
import com.example.scrumproyect.di.PresenterScope
import com.example.scrumproyect.di.module.PresenterModule
import dagger.Component

/**
 * @author @briansalvattore on 01/03/2018.
 */
@PresenterScope
@Component(dependencies = [AppComponent::class], modules = [PresenterModule::class])
interface PresenterComponent {

    fun inject(activity: MainActivity)
}