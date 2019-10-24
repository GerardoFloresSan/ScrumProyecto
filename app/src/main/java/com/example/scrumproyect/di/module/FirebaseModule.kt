package com.example.scrumproyect.di.module

import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class FirebaseModule {

    @Provides
    @Singleton
    fun firebaseFirestore() =  FirebaseFirestore.getInstance()

}