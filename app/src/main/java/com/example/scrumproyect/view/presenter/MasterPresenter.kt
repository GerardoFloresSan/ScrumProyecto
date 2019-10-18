package com.example.scrumproyect.view.presenter

import com.example.scrumproyect.data.entity.MasterEntity
import com.example.scrumproyect.view.presenter.base.BasePresenter
import com.example.scrumproyect.view.ui.utils.PapersManager
import com.google.firebase.firestore.FirebaseFirestore
import java.io.Serializable

class MasterPresenter : BasePresenter<MasterPresenter.View>() {
    private var fireBaseFireStore =  FirebaseFirestore.getInstance()

    fun syncMaster() {
        val getTask = fireBaseFireStore.collection("masters").document("data").get()

        getTask.addOnSuccessListener {
            view.takeIf { view != null }.apply {
                val master : MasterEntity = it.toObject(MasterEntity::class.java)!!
                PapersManager.masters = master
                view?.successMasters(0, master)
            }
        }
        getTask.addOnFailureListener { view?.successMasters(1)}
    }

    interface View : BasePresenter.View {
        fun successMasters(flag: Int, vararg args: Serializable)
    }
}