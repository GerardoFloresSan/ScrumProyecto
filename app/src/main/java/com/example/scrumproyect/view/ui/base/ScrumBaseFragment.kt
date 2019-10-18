package com.example.scrumproyect.view.ui.base

import android.content.Context
import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.MaterialDialog
import com.example.scrumproyect.R
import com.example.scrumproyect.di.Orchestrator

abstract class ScrumBaseFragment : BaseFragment() {

    protected var dialog: MaterialDialog? = null

    override fun getContext(): Context = this.activity?.applicationContext!!

    protected val googleToken by lazy { getString(R.string.default_web_client_id)}

    protected val component by lazy { Orchestrator.presenterComponent }

    fun showLoading() {
        hideLoading()
        dialog = MaterialDialog.Builder(activity!!)
            .title("Conectando...")
            .content("Espera un momento")
            .progress(true, 0)
            .cancelable(false)
            .show()
    }

    fun hideLoading() {
        if (dialog == null) return
        dialog?.dismiss()
        dialog = null
    }

    fun showError(message: String) {
        MaterialDialog.Builder(this.activity!!)
            .title("Advertencia")
            .content(message)
            .positiveText("Ok")
            .show()
    }

    fun replaceFragment(fragment: Fragment) {
        fragmentManager!!.beginTransaction().replace(R.id.content, fragment).commit()
    }

    fun showError(message: Int) {
        showError(getString(message))
    }

}