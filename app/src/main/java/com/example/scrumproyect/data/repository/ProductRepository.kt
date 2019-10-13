package com.example.scrumproyect.data.repository

import com.example.scrumproyect.domain.model.Product
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore

class ProductRepository(private val firestore: FirebaseFirestore) {
    fun fetchList(): Task<List<Product>> = firestore.collection("products").get().continueWith {
        val querySnapshot = it.result
        val list = ArrayList<Product>()
        if (querySnapshot != null) {
            for (documentSnapshot in querySnapshot.documents) {
                val session = documentSnapshot.toObject(Product::class.java) as Product
                list.add(session)
            }
        }
        list
    }
}