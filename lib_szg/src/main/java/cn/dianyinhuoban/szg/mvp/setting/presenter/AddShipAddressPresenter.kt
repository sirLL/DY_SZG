package cn.dianyinhuoban.szg.mvp.setting.presenter

import com.wareroom.lib_http.CustomResourceSubscriber
import cn.dianyinhuoban.szg.mvp.bean.AddressBean
import cn.dianyinhuoban.szg.mvp.bean.EmptyBean
import cn.dianyinhuoban.szg.mvp.setting.contract.AddShipAddressContract
import cn.dianyinhuoban.szg.mvp.setting.model.AddShipAddressModel
import com.wareroom.lib_base.mvp.BasePresenter
import com.wareroom.lib_base.utils.cache.MMKVUtil
import com.wareroom.lib_http.exception.ApiException
import com.wareroom.lib_http.response.ResponseTransformer
import com.wareroom.lib_http.schedulers.SchedulerProvider

class AddShipAddressPresenter(view: AddShipAddressContract.View) :
    BasePresenter<AddShipAddressModel, AddShipAddressContract.View>(view),
    AddShipAddressContract.Presenter {
    override fun buildModel(): AddShipAddressModel {
        return AddShipAddressModel()
    }

    override fun submitShipAddress(
        name: String,
        provinceId: String,
        cityId: String,
        districtId: String,
        area: String,
        address: String,
        phone: String,
        id: String
    ) {
        if (!isDestroy) {
            view?.showLoading()
        }
        mModel?.let {
            addDispose(
                it.submitShipAddress(name, provinceId, cityId, districtId, address, phone, id)
                    .compose(SchedulerProvider.getInstance().applySchedulers())
                    .compose(ResponseTransformer.handleResult())
                    .subscribeWith(object : CustomResourceSubscriber<String?>() {
                        override fun onError(exception: ApiException?) {
                            if (!isDestroy) {
                                view?.hideLoading()
                                handleError(exception)
                            }
                        }

                        override fun onNext(t: String) {
                            super.onNext(t)
                            if (!isDestroy) {
                                view?.hideLoading()
                                val address = AddressBean(
                                    address,
                                    area,
                                    cityId,
                                    districtId,
                                    t,
                                    "",
                                    "0",
                                    name,
                                    phone,
                                    provinceId,
                                    MMKVUtil.getUserID()
                                )
                                view?.onSubmitShipAddressSuccess(address)
                            }
                        }
                    })
            )
        }
    }

    override fun deleteAddress(ids: String) {
        if (!isDestroy) {
            view?.showLoading()
        }

        mModel?.let {
            addDispose(
                it.deleteAddress(ids)
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
                            if (!isDestroy) {
                                view?.hideLoading()
                                view?.onDeleteAddressSuccess(ids)
                            }
                        }
                    })
            )
        }
    }
}