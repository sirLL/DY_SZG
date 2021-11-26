package cn.dianyinhuoban.szg.mvp.machine.presenter

import com.wareroom.lib_http.CustomResourceSubscriber
import cn.dianyinhuoban.szg.mvp.bean.MyMachineBeanNew
import cn.dianyinhuoban.szg.mvp.machine.contract.MachineManagerNewContract
import cn.dianyinhuoban.szg.mvp.machine.model.MachineManagerNewModel
import com.wareroom.lib_base.mvp.BasePresenter
import com.wareroom.lib_http.exception.ApiException
import com.wareroom.lib_http.response.ResponseTransformer
import com.wareroom.lib_http.schedulers.SchedulerProvider

class MachineManagerNewPresenter(view: MachineManagerNewContract.View) :
    BasePresenter<MachineManagerNewModel, MachineManagerNewContract.View>(view),
    MachineManagerNewContract.Presenter {
    override fun buildModel(): MachineManagerNewModel {
        return MachineManagerNewModel()
    }

    override fun fetchMachine(
        type: String,
        status: String,
        sn: String,
        startDate: String,
        endDate: String,
        page: Int
    ) {
        mModel?.let {
            addDispose(
                it.fetchMachine(type, status, sn, startDate, endDate, page)
                    .compose(SchedulerProvider.getInstance().applySchedulers())
                    .compose(ResponseTransformer.handleResult())
                    .subscribeWith(object : CustomResourceSubscriber<MyMachineBeanNew?>() {
                        override fun onError(exception: ApiException?) {
                            handleError(exception)
                        }

                        override fun onNext(t: MyMachineBeanNew) {
                            super.onNext(t)
                            if (!isDestroy) {
                                view?.bindMachine(t)
                            }
                        }
                    })
            )
        }
    }
}