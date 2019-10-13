package com.example.scrumproyect.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**

 * @autor @RaúlEspinoza on 9/07/2018.

 */
@Module
class AppModule(val context: Context){

    @Provides
    @Singleton
    fun providesContext() = context
}