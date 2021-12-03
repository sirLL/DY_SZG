package cn.dianyinhuoban.szg.mvp.machine.presenter

import cn.dianyinhuoban.szg.mvp.bean.IntegralBalanceBean
import cn.dianyinhuoban.szg.mvp.bean.PurchaseProductBean
import cn.dianyinhuoban.szg.mvp.machine.contract.ExchangeContract
import cn.dianyinhuoban.szg.mvp.machine.model.ExchangeModel
import com.wareroom.lib_base.mvp.BasePresenter
import com.wareroom.lib_http.CustomResourceSubscriber
import com.wareroom.lib_http.exception.ApiException
import com.wareroom.lib_http.response.ResponseTransformer
import com.wareroom.lib_http.schedulers.SchedulerProvider

class ExchangePresenter(view : ExchangeContract.View):BasePresenter<ExchangeContract.Model,ExchangeContract.View>(view),ExchangeContract.Presenter {
    override fun buildModel(): ExchangeContract.Model {
        return ExchangeModel()
    }

    override fun fetchPurchaseProduct(typeID: String, page: Int) {
        mModel?.let {
            addDispose(
                it.fetchPurchaseProduct(typeID, page)
                    .compose(SchedulerProvider.getInstance().applySchedulers())
                    .compose(ResponseTransformer.handleResult())
                    .subscribeWith(object : CustomResourceSubscriber<List<PurchaseProductBean>?>() {
                        override fun onError(exception: ApiException?) {
                            handleError(exception)
                        }

                        override fun onNext(t: List<PurchaseProductBean>) {
                            super.onNext(t)
                            if (!isDestroy) {
                                view?.bindPurchaseProduct(t)
                            }
                        }
                    })
            )
        }
    }

    override fun fetchIntegralBalance(machineTypeId: String) {
        mModel?.let {
            addDispose(
                it.fetchIntegralBalance(machineTypeId)
                    .compose(SchedulerProvider.getInstance().applySchedulers())
                    .compose(ResponseTransformer.handleResult())
                    .subscribeWith(object : CustomResourceSubscriber<List<IntegralBalanceBean>?>() {
                        override fun onError(exception: ApiException?) {
                            if (!isDestroy) {
                                handleError(exception)
                            }
                        }

                        override fun onNext(t: List<IntegralBalanceBean>) {
                            super.onNext(t)
                            if (!isDestroy) {
                                view?.bindIntegralBalance(t)
                            }
                        }
                    })
            )
        }
    }
}