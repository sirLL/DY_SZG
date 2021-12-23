package cn.dianyinhuoban.szg.mvp.income.presenter

import cn.dianyinhuoban.szg.mvp.bean.IncomeRecordDetailBean
import cn.dianyinhuoban.szg.mvp.income.contract.IncomeRecordContract
import cn.dianyinhuoban.szg.mvp.income.model.IncomeRecordModel
import com.wareroom.lib_base.mvp.BasePresenter
import com.wareroom.lib_http.CustomResourceSubscriber
import com.wareroom.lib_http.exception.ApiException
import com.wareroom.lib_http.response.ResponseTransformer
import com.wareroom.lib_http.schedulers.SchedulerProvider

class IncomeRecordPresenter(view: IncomeRecordContract.View) :
    BasePresenter<IncomeRecordContract.Model, IncomeRecordContract.View>(view),
    IncomeRecordContract.Presenter {
    override fun buildModel(): IncomeRecordContract.Model {
        return IncomeRecordModel()
    }

    override fun fetchIncomeRecordDetail(id: String) {
        if (!isDestroy) {
            view?.showLoading()
        }
        mModel?.let {
            it.fetchIncomeRecordDetail(id)
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .compose(ResponseTransformer.handleResult())
                .subscribeWith(object : CustomResourceSubscriber<IncomeRecordDetailBean?>() {
                    override fun onError(exception: ApiException?) {
                        handleError(exception)
                    }

                    override fun onNext(t: IncomeRecordDetailBean) {
                        super.onNext(t)
                        if (!isDestroy) {
                            view?.hideLoading()
                            view?.bindIncomeRecordDetail(t)
                        }
                    }
                })
        }
    }
}