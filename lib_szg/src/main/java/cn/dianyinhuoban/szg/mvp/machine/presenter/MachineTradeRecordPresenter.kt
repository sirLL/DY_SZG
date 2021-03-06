package cn.dianyinhuoban.szg.mvp.machine.presenter

import com.wareroom.lib_http.CustomResourceSubscriber
import cn.dianyinhuoban.szg.mvp.bean.MachineTradeRecordBean
import cn.dianyinhuoban.szg.mvp.machine.contract.MachineTradeRecordContract
import cn.dianyinhuoban.szg.mvp.machine.model.MachineTradeRecordModel
import com.wareroom.lib_base.mvp.BasePresenter
import com.wareroom.lib_http.exception.ApiException
import com.wareroom.lib_http.response.ResponseTransformer
import com.wareroom.lib_http.schedulers.SchedulerProvider

class MachineTradeRecordPresenter(view: MachineTradeRecordContract.View) :
    BasePresenter<MachineTradeRecordModel, MachineTradeRecordContract.View>(view),
    MachineTradeRecordContract.Presenter {
    override fun buildModel(): MachineTradeRecordModel {
        return MachineTradeRecordModel()
    }

    override fun fetchTradeRecord(sn: String, page: Int) {
        mModel?.let {
            addDispose(
                it.fetchTradeRecord(sn, page)
                    .compose(SchedulerProvider.getInstance().applySchedulers())
                    .compose(ResponseTransformer.handleResult())
                    .subscribeWith(object :
                        CustomResourceSubscriber<List<MachineTradeRecordBean>?>() {
                        override fun onError(exception: ApiException?) {
                            handleError(exception)
                        }

                        override fun onNext(t: List<MachineTradeRecordBean>) {
                            super.onNext(t)
                            if (!isDestroy) {
                                view?.bindTradeRecord(t)
                            }
                        }
                    })
            )
        }
    }
}