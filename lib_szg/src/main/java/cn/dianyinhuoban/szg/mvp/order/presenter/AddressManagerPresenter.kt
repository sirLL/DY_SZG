package cn.dianyinhuoban.szg.mvp.order.presenter

import com.wareroom.lib_http.CustomResourceSubscriber
import cn.dianyinhuoban.szg.mvp.bean.AddressBean
import cn.dianyinhuoban.szg.mvp.order.contract.AddressManagerContract
import cn.dianyinhuoban.szg.mvp.order.model.AddressManagerModel
import com.wareroom.lib_base.mvp.BasePresenter
import com.wareroom.lib_http.exception.ApiException
import com.wareroom.lib_http.response.ResponseTransformer
import com.wareroom.lib_http.schedulers.SchedulerProvider

class AddressManagerPresenter(view: AddressManagerContract.View) :
    BasePresenter<AddressManagerModel, AddressManagerContract.View>(view),
    AddressManagerContract.Presenter {
    override fun buildModel(): AddressManagerModel {
        return AddressManagerModel()
    }

    override fun fetchAddressList(page: Int) {
        mModel?.let {
            addDispose(
                it.fetchAddressList(page)
                    .compose(SchedulerProvider.getInstance().applySchedulers())
                    .compose(ResponseTransformer.handleResult())
                    .subscribeWith(object : CustomResourceSubscriber<List<AddressBean>?>() {
                        override fun onError(exception: ApiException?) {
                            handleError(exception)
                        }

                        override fun onNext(t: List<AddressBean>) {
                            super.onNext(t)
                            if (!isDestroy) {
                                view?.bindAddressList(t)
                            }
                        }
                    })
            )
        }
    }
}