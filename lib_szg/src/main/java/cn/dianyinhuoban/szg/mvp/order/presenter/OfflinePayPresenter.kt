package cn.dianyinhuoban.szg.mvp.order.presenter

import cn.dianyinhuoban.szg.mvp.bean.OfflinePayInfoBean
import cn.dianyinhuoban.szg.mvp.bean.PayInfoBean
import cn.dianyinhuoban.szg.mvp.bean.UploadResultBean
import cn.dianyinhuoban.szg.mvp.order.contract.OfflinePayContract
import cn.dianyinhuoban.szg.mvp.order.model.OfflinePayModel
import com.wareroom.lib_base.mvp.BasePresenter
import com.wareroom.lib_http.CustomResourceSubscriber
import com.wareroom.lib_http.exception.ApiException
import com.wareroom.lib_http.response.ResponseTransformer
import com.wareroom.lib_http.schedulers.SchedulerProvider
import java.io.File

class OfflinePayPresenter(view: OfflinePayContract.View) :
    BasePresenter<OfflinePayContract.Model, OfflinePayContract.View>(view),
    OfflinePayContract.Presenter {
    override fun buildModel(): OfflinePayContract.Model {
        return OfflinePayModel()
    }

    override fun fetchOfflinePayInfo() {
        if (!isDestroy) {
            view?.showLoading()
        }
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
                                view?.hideLoading()
                                view?.bindOfflinePayInfo(t)
                            }
                        }
                    })
            )
        }
    }

    override fun uploadVoucher(file: File) {
        if (!isDestroy) {
            view?.showLoading()
        }
        mModel?.let {
            addDispose(
                it.uploadVoucher(file)
                    .compose(SchedulerProvider.getInstance().applySchedulers())
                    .compose(ResponseTransformer.handleResult())
                    .subscribeWith(object : CustomResourceSubscriber<UploadResultBean?>() {
                        override fun onError(exception: ApiException?) {
                            handleError(exception)
                        }


                        override fun onNext(t: UploadResultBean) {
                            super.onNext(t)
                            view?.hideLoading()
                            view?.bindVoucher(t)
                        }
                    })
            )
        }
    }

    override fun submitPurchaseOrder(
        productID: String,
        num: String,
        addressID: String,
        payName: String,
        bankNo: String,
        bankName: String,
        voucher: String,
        password: String
    ) {
        if (!isDestroy) {
            view?.showLoading()
        }
        mModel?.let {
            addDispose(
                it.submitPurchaseOrder(
                    productID,
                    num,
                    addressID,
                    payName,
                    bankNo,
                    bankName,
                    voucher,
                    password
                )
                    .compose(SchedulerProvider.getInstance().applySchedulers())
                    .compose(ResponseTransformer.handleResult())
                    .subscribeWith(object : CustomResourceSubscriber<PayInfoBean?>() {
                        override fun onError(exception: ApiException?) {
                            handleError(exception)
                        }

                        override fun onNext(t: PayInfoBean) {
                            super.onNext(t)
                            if (!isDestroy) {
                                view?.hideLoading()
                                view?.onSubmitOrderSuccess()
                            }
                        }
                    })
            )
        }
    }
}