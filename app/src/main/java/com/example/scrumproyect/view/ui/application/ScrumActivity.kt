package com.example.scrumproyect.view.ui.application

import android.app.Activity
import android.app.Application
import android.content.Intent
import com.example.scrumproyect.di.component.AppComponent
/*import com.example.scrumproyect.di.component.DaggerPresenterComponent*/
import com.example.scrumproyect.di.module.AppModule
import com.google.android.gms.security.ProviderInstaller
import java.lang.Exception
import java.util.*

open class ScrumActivity : Application() {

    override fun onCreate() {
        super.onCreate()

        /*appComponent = DaggerAppComponent.builder().appModule(AppModule(this)).build()*/

        ProviderInstaller.installIfNeededAsync(this, object : ProviderInstaller.ProviderInstallListener {

            @Suppress("MayBeConstant")
            private val TAG = "ProviderInstaller"

            override fun onProviderInstallFailed(p0: Int, p1: Intent?) {
            }

            override fun onProviderInstalled() {
            }
        })

    }

    companion object {

        /*lateinit var appComponent: AppComponent*/

        private val activities = Stack<Activity>()

        fun exitsActivities() : Boolean {
            return activities.isEmpty()
        }

        fun addActivity(activity: Activity) {
            activities.add(activity)
        }

        fun removeActivity(activity: Activity) {
            activities.remove(activity)
        }

        fun closeAll() {
            for (activity in activities) {
                try {
                    activity.finish()
                } catch (ignore: Exception) {
                }
            }
            activities.clear()
        }
    }
}