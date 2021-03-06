package cn.dianyinhuoban.szg.mvp.setting.presenter

import com.wareroom.lib_http.CustomResourceSubscriber
import cn.dianyinhuoban.szg.mvp.bean.BankBean
import cn.dianyinhuoban.szg.mvp.bean.EmptyBean
import cn.dianyinhuoban.szg.mvp.bean.ImageCodeBean
import cn.dianyinhuoban.szg.mvp.setting.contract.BankContract
import cn.dianyinhuoban.szg.mvp.setting.model.BankModel
import com.wareroom.lib_base.mvp.BasePresenter
import com.wareroom.lib_http.exception.ApiException
import com.wareroom.lib_http.response.ResponseTransformer
import com.wareroom.lib_http.schedulers.SchedulerProvider


class BankPresenter(view: BankContract.View) : BasePresenter<BankModel, BankContract.View>(view),

    BankContract.Presenter {
    override fun buildModel(): BankModel {
        return BankModel()
    }

    override fun getBankList() {
        if (!isDestroy) {
            view?.showLoading()
            mModel?.let {
                addDispose(
                    it.getBankList()
                        .compose(SchedulerProvider.getInstance().applySchedulers())
                        .compose(ResponseTransformer.handleResult())
                        .subscribeWith(object : CustomResourceSubscriber<List<BankBean>?>() {
                            override fun onError(exception: ApiException?) {
                                if (!isDestroy) {
                                    view?.hideLoading()
                                    handleError(exception)
                                }
                            }

                            override fun onNext(t: List<BankBean>) {
                                super.onNext(t)
                                if (!isDestroy) {
                                    view?.hideLoading()
                                    view?.onLoadBankList(t)
                                }
                            }
                        })
                )
            }
        }
    }

    override fun addBank(
        name: String,
        bankName: String,
        bankNo: String,
        phone: String,
        code: String
    ) {
        if (!isDestroy) {
            view?.showLoading()
            mModel?.let {
                addDispose(
                    it.addBank(name, bankName, bankNo, phone, code)
                        .compose(SchedulerProvider.getInstance().applySchedulers())
                        .compose(ResponseTransformer.handleResult())
                        .subscribeWith(object : CustomResourceSubscriber<EmptyBean?>() {
                            override fun onError(exception: ApiException?) {
                                if (!isDestroy) {
                                    view?.hideLoading()
                                    handleError(exception)
                                }
                            }

                            override fun onNext(t: EmptyBean) {
                                super.onNext(t)
                                if (!isDestroy)
                                    view?.hideLoading()
                                    view?.onAddBankSuccess()
                            }
                        })
                )
            }
        }


    }

    override fun updateBank(
        name: String,
        bankName: String,
        bankNo: String,
        phone: String,
        code: String,
        id: String
    ) {

        if (!isDestroy) {
            view?.showLoading()
            mModel?.let {
                addDispose(
                    it.updateBank(name, bankName, bankNo, phone, code, id)
                        .compose(SchedulerProvider.getInstance().applySchedulers())
                        .compose(ResponseTransformer.handleResult())
                        .subscribeWith(object : CustomResourceSubscriber<EmptyBean?>() {
                            override fun onError(exception: ApiException?) {
                                if (!isDestroy) {
                                    view?.hideLoading()
                                    handleError(exception)
                                }
                            }

                            override fun onNext(t: EmptyBean) {
                                super.onNext(t)
                                if (!isDestroy)
                                    view?.hideLoading()
                                    view?.onUpdateBankSuccess()
                            }
                        })
                )
            }
        }
    }

    override fun setBank(type: String, id: String) {
        if (!isDestroy) {
            view?.showLoading()
            mModel?.let {
                addDispose(
                    it.setBank(type, id)
                        .compose(SchedulerProvider.getInstance().applySchedulers())
                        .compose(ResponseTransformer.handleResult())
                        .subscribeWith(object : CustomResourceSubscriber<EmptyBean?>() {
                            override fun onComplete() {
                                super.onComplete()
                                if (!isDestroy) {
                                    view?.hideLoading()
                                }
                            }

                            override fun onError(exception: ApiException?) {
                                if (!isDestroy) {
                                    view?.hideLoading()
                                    handleError(exception)
                                }
                            }

                            override fun onNext(t: EmptyBean) {
                                super.onNext(t)
                                if (!isDestroy)
                                    view?.hideLoading()
                                view?.onSetSuccess()
                            }
                        })
                )
            }
        }
    }

    override fun fetchImageCode() {
        mModel?.let {
            addDispose(
                it.fetchImageCode()
                    .compose(SchedulerProvider.getInstance().applySchedulers())
                    .compose(ResponseTransformer.handleResult())
                    .subscribeWith(object : CustomResourceSubscriber<ImageCodeBean?>() {
                        override fun onError(exception: ApiException?) {
                            if (!isDestroy) {
                                view?.hideLoading()
                                handleError(exception)
                            }
                        }

                        override fun onNext(t: ImageCodeBean) {
                            super.onNext(t)
                            if (!isDestroy) {
                                view?.showImageCode(t)
                            }
                        }
                    })
            )
        }
    }

    //????????????
    override fun onSendSMS(phone: String, imageKey: String, imageCode: String) {
        mModel?.let {
            it.sendSMS(phone, imageKey, imageCode)
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .compose(ResponseTransformer.handleResult())
                .subscribeWith(object : CustomResourceSubscriber<EmptyBean?>() {
                    override fun onError(exception: ApiException?) {
                        if (!isDestroy) {
                            view?.hideLoading()
                            handleError(exception)
                        }
                    }

                    override fun onNext(t: EmptyBean) {
                        super.onNext(t)
                        //??????????????????????????????????????????
                        if (!isDestroy) {
                            view?.startSMSCountdown()
                        }
                    }
                })
        }
    }


}