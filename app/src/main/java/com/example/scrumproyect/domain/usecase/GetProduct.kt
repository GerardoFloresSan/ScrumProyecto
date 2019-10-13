package com.example.scrumproyect.domain.usecase

import com.example.scrumproyect.data.repository.ProductRepository
import com.example.scrumproyect.domain.usecase.base.UseCase
import com.example.scrumproyect.domain.model.Product
import com.example.scrumproyect.view.viewModel.ProductViewModel
import io.reactivex.Observable
import io.reactivex.Scheduler

class GetProduct(
    executorThread: Scheduler,
    uiThread: Scheduler,
    private var productsRepository: ProductRepository
) : UseCase<ArrayList<ProductViewModel>>(executorThread, uiThread) {


    override fun createObservableUseCase(): Observable<ArrayList<ProductViewModel>> {
        val product = productsRepository.fetchList()

        return Observable.create<ArrayList<ProductViewModel>> { observable ->
            product.addOnSuccessListener {
                val productList = product.result as List<Product>
                val list = ArrayList<ProductViewModel>()

                productList.forEach { _p ->
                    list.add(
                        ProductViewModel().apply {
                            id = _p.id
                            title = _p.title
                        }
                    )
                }

                observable.onNext(list)
                observable.onComplete()
            }
            product.addOnFailureListener { e ->
                observable.onError(e)
            }
        }
    }
}
