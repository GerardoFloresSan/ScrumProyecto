package com.example.scrumproyect.di

import com.example.scrumproyect.view.ui.application.ScrumApplication
import com.example.scrumproyect.di.component.PresenterComponent
import com.example.scrumproyect.di.component.DaggerPresenterComponent
import javax.inject.Singleton

@Singleton
object Orchestrator {
    val presenterComponent: PresenterComponent by lazy {
        DaggerPresenterComponent
                .builder()
                .appComponent(ScrumApplication.appComponent)
                .build()
    }
}