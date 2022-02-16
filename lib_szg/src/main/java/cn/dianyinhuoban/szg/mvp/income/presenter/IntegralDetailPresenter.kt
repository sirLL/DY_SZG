package cn.dianyinhuoban.szg.mvp.income.presenter

import cn.dianyinhuoban.szg.mvp.bean.IntegralDetailBean
import cn.dianyinhuoban.szg.mvp.income.contract.IntegralDetailContract
import cn.dianyinhuoban.szg.mvp.income.model.IntegralDetailModel
import com.wareroom.lib_base.mvp.BasePresenter
import com.wareroom.lib_http.CustomResourceSubscriber
import com.wareroom.lib_http.exception.ApiException
import com.wareroom.lib_http.response.ResponseTransformer
import com.wareroom.lib_http.schedulers.SchedulerProvider

class IntegralDetailPresenter(view: IntegralDetailContract.View) :
    BasePresenter<IntegralDetailContract.Model, IntegralDetailContract.View>(view),
    IntegralDetailContract.Presenter {
    override fun buildModel(): IntegralDetailContract.Model {
        return IntegralDetailModel()
    }

    override fun fetchIntegralDetail(recordID: String) {
        if (!isDestroy) {
            view?.showLoading()
        }
        mModel?.let {
            addDispose(
                it.fetchIntegralDetail(recordID)
                    .compose(SchedulerProvider.getInstance().applySchedulers())
                    .compose(ResponseTransformer.handleResult())
                    .subscribeWith(object : CustomResourceSubscriber<IntegralDetailBean?>() {
                        override fun onError(exception: ApiException?) {
                            if (!isDestroy) {
                                view?.hideLoading()
                                handleError(exception)
                            }
                        }

                        override fun onNext(t: IntegralDetailBean) {
                            super.onNext(t)
                            if (!isDestroy) {
                                view?.hideLoading()
                                view?.bindIntegralDetail(t)
                            }
                        }
                    })
            )
        }
    }
}