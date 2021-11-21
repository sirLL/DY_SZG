package cn.dianyinhuoban.szg.mvp.machine.presenter

import com.wareroom.lib_http.CustomResourceSubscriber
import cn.dianyinhuoban.szg.mvp.bean.TransferRecordDetailBean
import cn.dianyinhuoban.szg.mvp.machine.contract.TransferRecordDetailContract
import cn.dianyinhuoban.szg.mvp.machine.model.TransferRecordDetailModel
import com.wareroom.lib_base.mvp.BasePresenter
import com.wareroom.lib_http.exception.ApiException
import com.wareroom.lib_http.response.ResponseTransformer
import com.wareroom.lib_http.schedulers.SchedulerProvider

class TransferRecordDetailPresenter(view: TransferRecordDetailContract.View) :
    BasePresenter<TransferRecordDetailModel, TransferRecordDetailContract.View>(view),
    TransferRecordDetailContract.Presenter {
    override fun buildModel(): TransferRecordDetailModel {
        return TransferRecordDetailModel()
    }

    override fun fetchTransferRecordDetail(recordID: String, page: Int) {
        mModel?.let {
            addDispose(
                it.fetchTransferRecordDetail(recordID, page)
                    .compose(SchedulerProvider.getInstance().applySchedulers())
                    .compose(ResponseTransformer.handleResult())
                    .subscribeWith(object :
                        CustomResourceSubscriber<List<TransferRecordDetailBean>?>() {
                        override fun onError(exception: ApiException?) {
                            handleError(exception)
                        }

                        override fun onNext(t: List<TransferRecordDetailBean>) {
                            super.onNext(t)
                            if (!isDestroy) {
                                view?.bindRecordDetail(t)
                            }
                        }
                    })
            )
        }
    }
}