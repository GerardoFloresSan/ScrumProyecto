package com.example.scrumproyect.di.component

import com.example.scrumproyect.view.ui.activity.MainActivity
import com.example.scrumproyect.di.PresenterScope
import com.example.scrumproyect.di.module.PresenterModule
import dagger.Component

@PresenterScope
@Component(dependencies = [AppComponent::class], modules = [PresenterModule::class])
interface PresenterComponent {
    fun inject(activity: MainActivity)
}