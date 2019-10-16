package com.example.scrumproyect.view.ui.fragment

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.scrumproyect.R
import com.example.scrumproyect.data.entity.ProductEntity
import com.example.scrumproyect.view.presenter.ProductPresenter
import com.example.scrumproyect.view.ui.activity.DetailProductActivity
import com.example.scrumproyect.view.ui.adapter.ProductAdapter
import com.example.scrumproyect.view.ui.base.ScrumBaseFragment
import kotlinx.android.synthetic.main.fragment_home.*
import java.io.Serializable

class HomeFragment : ScrumBaseFragment(), ProductPresenter.View{

    private val presenter = ProductPresenter()

    override fun getFragmentView() = R.layout.fragment_home

    override fun onCreate() {
        setTitle("Home")
    }

    override fun onResume() {
        super.onResume()
        presenter.attachView(this)
        presenter.syncProduct()
    }

    override fun onPause() {
        super.onPause()
        presenter.detachView()
    }

    @Suppress("UNCHECKED_CAST", "USELESS_CAST")
    override fun successSchedule(flag: Int, vararg args: Serializable) {
        if (flag == 0) {
            recycler.layoutManager = GridLayoutManager(context, 1) as RecyclerView.LayoutManager?
            recycler.adapter = ProductAdapter(args[0] as List<ProductEntity>) {
                startActivity(DetailProductActivity::class.java, it)
            }
        }
    }
}
