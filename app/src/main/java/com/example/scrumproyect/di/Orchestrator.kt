package com.example.scrumproyect.di

import com.example.scrumproyect.di.component.PresenterComponent
/*import com.example.scrumproyect.di.component.DaggerPresenterComponent*/
import com.example.scrumproyect.view.ui.application.ScrumActivity
import javax.inject.Singleton

/**
 * @author @briansalvattore on 01/03/2018.
 */
@Singleton
object Orchestrator {
/*    val presenterComponent: PresenterComponent by lazy {
        DaggerPresenterComponent
                .builder()
                .appComponent(ScrumActivity.appComponent)
                .build()
    }*/
}