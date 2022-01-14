package cn.dianyinhuoban.szg.mvp.order.presenter

import cn.dianyinhuoban.szg.mvp.bean.IntegralBalanceBean
import cn.dianyinhuoban.szg.mvp.bean.OfflinePayInfoBean
import cn.dianyinhuoban.szg.mvp.order.contract.PayTypeContract
import cn.dianyinhuoban.szg.mvp.order.model.PayTypeModel
import com.wareroom.lib_base.mvp.BasePresenter
import com.wareroom.lib_http.CustomResourceSubscriber
import com.wareroom.lib_http.exception.ApiException
import com.wareroom.lib_http.response.ResponseTransformer
import com.wareroom.lib_http.schedulers.SchedulerProvider

class PayTypePresenter(view: PayTypeContract.View) :
    BasePresenter<PayTypeContract.Model, PayTypeContract.View>(view), PayTypeContract.Presenter {
    override fun buildModel(): PayTypeContract.Model {
        return PayTypeModel()
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

    override fun fetchOfflinePayInfo() {
        mModel?.let {
            addDispose(
                it.fetchOfflinePayInfo()
                    .compose(SchedulerProvider.getInstance().applySchedulers())
                    .compose(ResponseTransformer.handleResult())
                    .subscribeWith(object : CustomResourceSubscriber<OfflinePayInfoBean?>() {
                        override fun onError(exception: ApiException?) {
                            handleError(exception)
                        }

                        override fun onNext(t: OfflinePayInfoBean) {
                            super.onNext(t)
                            if (!isDestroy) {
                                view?.bindOfflinePayInfo(t)
                            }
                        }
                    })
            )
        }
    }
}