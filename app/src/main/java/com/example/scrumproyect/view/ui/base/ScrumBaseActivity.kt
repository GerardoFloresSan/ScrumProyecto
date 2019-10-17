package com.example.scrumproyect.view.ui.base

import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.MaterialDialog
import com.example.scrumproyect.R
import com.example.scrumproyect.di.Orchestrator

abstract class ScrumBaseActivity : BaseActivity() {
    protected var dialog: MaterialDialog? = null

    protected val component by lazy { Orchestrator.presenterComponent }

    protected val googleToken by lazy { getString(R.string.default_web_client_id)}



    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.content, fragment).commit()
    }

    fun getContext() = this

    fun showLoading() {
        hideLoading()
        dialog = MaterialDialog.Builder(this)
            .title("Cargando...")
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

    private fun getErrorDialog(message: String) = MaterialDialog.Builder(this)
        .title("Advertencia")
        .content(message)
        .positiveText("Ok")

    fun showError(message: String) {
        getErrorDialog(message).show()
    }

    fun showError(message: Int){
        showError(getString(message))
    }

}