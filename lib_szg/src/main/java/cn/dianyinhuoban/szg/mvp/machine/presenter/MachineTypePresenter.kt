package cn.dianyinhuoban.szg.mvp.machine.presenter

import com.wareroom.lib_http.CustomResourceSubscriber
import cn.dianyinhuoban.szg.mvp.bean.MachineTypeBean
import cn.dianyinhuoban.szg.mvp.machine.contract.MachineTypeContract
import cn.dianyinhuoban.szg.mvp.machine.model.MachineTypeModel
import com.wareroom.lib_base.mvp.BasePresenter
import com.wareroom.lib_http.exception.ApiException
import com.wareroom.lib_http.response.ResponseTransformer
import com.wareroom.lib_http.schedulers.SchedulerProvider

class MachineTypePresenter(view: MachineTypeContract.View) :
    BasePresenter<MachineTypeModel, MachineTypeContract.View>(view), MachineTypeContract.Presenter {
    override fun buildModel(): MachineTypeModel {
        return MachineTypeModel()
    }

    override fun fetchMachineType() {
        mModel?.let {
            addDispose(
                it.fetchMachineType()
                    .compose(SchedulerProvider.getInstance().applySchedulers())
                    .compose(ResponseTransformer.handleResult())
                    .subscribeWith(object : CustomResourceSubscriber<List<MachineTypeBean>?>() {
                        override fun onError(exception: ApiException?) {
                            handleError(exception)
                        }

                        override fun onNext(t: List<MachineTypeBean>) {
                            super.onNext(t)
                            if (!isDestroy) {
                                view?.bindMachineType(t)
                            }
                        }
                    })
            )
        }
    }

}