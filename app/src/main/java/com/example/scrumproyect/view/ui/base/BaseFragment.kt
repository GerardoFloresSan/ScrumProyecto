package com.example.scrumproyect.view.ui.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.scrumproyect.di.Orchestrator
import com.example.scrumproyect.view.ui.extensions.enableError
import com.google.android.material.textfield.TextInputLayout
import java.io.Serializable

abstract class BaseFragment :Fragment(){


    private lateinit var parent: View

    override fun getContext(): Context = this.activity?.applicationContext!!

    protected val component by lazy { Orchestrator.presenterComponent }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(getFragmentView(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parent = view
        onCreate()
    }

    @SuppressWarnings("unused")
    protected fun startActivity(cls: Class<out AppCompatActivity>, vararg extras: Serializable) {

        val intent = Intent(activity, cls)

        for (i in extras.indices) {
            intent.putExtra("extra" + i, extras[i])
        }

        startActivity(intent)
    }

    abstract fun getFragmentView(): Int
    abstract fun onCreate()

    protected fun setTitle(title: String) {
        if (activity is BaseActivity) {
            (activity as BaseActivity).setTitle(title)
        }
    }

    fun activeAllWrappers() {
        activeAllWrappers(parent as ViewGroup)
    }

    fun hideAllWrappers() {
        activeAllWrappers(parent as ViewGroup)
    }

    private fun activeAllWrappers(parent: ViewGroup) {
        for (i in 0..parent.childCount) {

            val view = parent.getChildAt(i)

            if (view is TextInputLayout) {
                view.enableError()
            } else if (view is ViewGroup) {
                activeAllWrappers(parent.getChildAt(i) as ViewGroup)
            }
        }
    }
}