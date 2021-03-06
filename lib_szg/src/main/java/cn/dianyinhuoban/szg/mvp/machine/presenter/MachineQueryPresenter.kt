package cn.dianyinhuoban.szg.mvp.machine.presenter

import com.wareroom.lib_http.CustomResourceSubscriber

import cn.dianyinhuoban.szg.mvp.bean.MachineTypeBean
import cn.dianyinhuoban.szg.mvp.bean.MyMachineBean
import cn.dianyinhuoban.szg.mvp.machine.contract.MachineQueryContract
import cn.dianyinhuoban.szg.mvp.machine.model.MachineQueryModel
import com.wareroom.lib_base.mvp.BasePresenter
import com.wareroom.lib_http.exception.ApiException
import com.wareroom.lib_http.response.ResponseTransformer
import com.wareroom.lib_http.schedulers.SchedulerProvider

class MachineQueryPresenter(view: MachineQueryContract.View) :
    BasePresenter<MachineQueryModel, MachineQueryContract.View>(view),
    MachineQueryContract.Presenter {
    override fun buildModel(): MachineQueryModel {
        return MachineQueryModel()
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

    override fun fetchMachine(type: String, status: String, sn: String, backMoney: String, page: Int) {
        mModel?.let {
            addDispose(
                it.fetchMachine(type, status, sn, backMoney, page)
                    .compose(SchedulerProvider.getInstance().applySchedulers())
                    .compose(ResponseTransformer.handleResult())
                    .subscribeWith(object : CustomResourceSubscriber<MyMachineBean?>() {
                        override fun onError(exception: ApiException?) {
                            handleError(exception)
                        }

                        override fun onNext(t: MyMachineBean) {
                            super.onNext(t)
                            if (!isDestroy) {
                                view?.bindMachine(t)
                            }
                        }
                    })
            )
        }
    }

    override fun fetchMachineBySN(type: String, sn: String, backMoney: String, page: Int, requestCode: Int) {
        if (!isDestroy) {
            view?.showLoading(false)
        }
        mModel?.let {
            addDispose(
                it.fetchMachine(type, "", sn, backMoney, page)
                    .compose(SchedulerProvider.getInstance().applySchedulers())
                    .compose(ResponseTransformer.handleResult())
                    .subscribeWith(object : CustomResourceSubscriber<MyMachineBean?>() {
                        override fun onError(exception: ApiException?) {
                            handleError(exception)
                        }

                        override fun onNext(t: MyMachineBean) {
                            super.onNext(t)
                            if (!isDestroy) {
                                view?.hideLoading()
                                view?.bindMachineBySN(t, requestCode)
                            }
                        }
                    })
            )
        }
    }


}