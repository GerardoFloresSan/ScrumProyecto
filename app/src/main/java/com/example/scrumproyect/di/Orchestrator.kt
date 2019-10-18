package com.example.scrumproyect.di

import com.example.scrumproyect.view.ui.application.ScrumApplication
import com.example.scrumproyect.di.component.PresenterComponent
import com.example.scrumproyect.di.component.DaggerPresenterComponent
import javax.inject.Singleton

/**
 * @author @briansalvattore on 01/03/2018.
 */
@Singleton
object Orchestrator {
    val presenterComponent: PresenterComponent by lazy {
        DaggerPresenterComponent
                .builder()
                .appComponent(ScrumApplication.appComponent)
                .build()
    }
}