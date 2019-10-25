package com.example.scrumproyect.view.ui.application

import android.app.Activity
import android.app.Application
import android.content.Intent
import com.example.scrumproyect.di.component.AppComponent
import com.example.scrumproyect.di.component.DaggerAppComponent
import com.example.scrumproyect.di.module.AppModule
import com.example.scrumproyect.view.ui.utils.Methods
import com.google.android.gms.security.ProviderInstaller
import io.paperdb.Paper
import java.lang.Exception
import java.util.*

open class ScrumApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Paper.init(this)
        Methods.init(this)
        appComponent = DaggerAppComponent.builder().appModule(AppModule(this)).build()

        println("   - - - - - - - -       - - - - - - - -  ")
        println("  //             \\     //              \\")
        println(" //               \\ _ //                \\")
        println("//                                       \\")
        println(" \\                                     //")
        println("   \\                                 //")
        println("     \\     Ella si tiene corazon   //")
        println("       \\     Pero no para ti     //")
        println("         \\                     //")
        println("           \\                 //")
        println("             \\             //")
        println("               \\         //")
        println("                 \\     //")
        println("                   \\ //")

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

        lateinit var appComponent: AppComponent

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