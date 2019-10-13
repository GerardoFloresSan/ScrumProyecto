package com.example.scrumproyect.di.module

import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @author @briansalvattore on 14/09/2018.
 */
@Module
class FirebaseModule {

    @Provides
    @Singleton
    fun firebaseFirestore() =  FirebaseFirestore.getInstance()

}