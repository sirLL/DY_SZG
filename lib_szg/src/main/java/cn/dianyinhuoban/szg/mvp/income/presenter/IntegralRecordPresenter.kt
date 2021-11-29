package cn.dianyinhuoban.szg.mvp.income.presenter

import cn.dianyinhuoban.szg.bean.IntegralRecordBean
import cn.dianyinhuoban.szg.mvp.income.contract.IntegralRecordContract
import cn.dianyinhuoban.szg.mvp.income.model.IntegralRecordModel
import com.wareroom.lib_base.mvp.BasePresenter
import com.wareroom.lib_http.CustomResourceSubscriber
import com.wareroom.lib_http.exception.ApiException
import com.wareroom.lib_http.response.ResponseTransformer
import com.wareroom.lib_http.schedulers.SchedulerProvider

class IntegralRecordPresenter(view: IntegralRecordContract.View) :
    BasePresenter<IntegralRecordContract.Model, IntegralRecordContract.View>(view),
    IntegralRecordContract.Presenter {
    override fun buildModel(): IntegralRecordContract.Model {
        return IntegralRecordModel()
    }

    override fun fetchIntegralRecord(machineType: String, status: String, page: Int) {
        mModel?.let {
            it.fetchIntegralRecord(machineType, status, page)
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .compose(ResponseTransformer.handleResult())
                .subscribeWith(object : CustomResourceSubscriber<List<IntegralRecordBean>?>() {
                    override fun onError(exception: ApiException?) {
                        handleError(exception)
                    }

                    override fun onNext(t: List<IntegralRecordBean>) {
                        super.onNext(t)
                        if (!isDestroy) {
                            view?.bindIntegralRecord(t)
                        }
                    }
                })
        }
    }
}