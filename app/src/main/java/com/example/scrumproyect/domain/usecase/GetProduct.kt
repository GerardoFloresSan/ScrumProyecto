package com.example.scrumproyect.domain.usecase

import com.example.scrumproyect.data.entity.ArticleEntity
import com.example.scrumproyect.data.repository.ProductRepository
import com.example.scrumproyect.domain.usecase.base.UseCase
import com.example.scrumproyect.domain.model.Product
import io.reactivex.Observable
import io.reactivex.Scheduler

class GetProduct(
    executorThread: Scheduler,
    uiThread: Scheduler,
    private var productsRepository: ProductRepository
) : UseCase<ArrayList<ArticleEntity>>(executorThread, uiThread) {


    override fun createObservableUseCase(): Observable<ArrayList<ArticleEntity>> {
        val product = productsRepository.fetchList()

        return Observable.create<ArrayList<ArticleEntity>> { observable ->
            product.addOnSuccessListener {
                val productList = product.result as List<Product>
                val list = ArrayList<ArticleEntity>()

                productList.forEach { _p ->
                    list.add(
                        ArticleEntity().apply {
                            idM = _p.idM
                            titleM = _p.titleM
                            descriptionM = _p.descriptionM
                            urlImageM = _p.urlImageM
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
