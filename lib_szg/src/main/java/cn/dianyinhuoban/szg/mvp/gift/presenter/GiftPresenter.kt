package cn.dianyinhuoban.szg.mvp.gift.presenter

import cn.dianyinhuoban.szg.bean.GiftInfoBean
import cn.dianyinhuoban.szg.mvp.bean.EmptyBean
import cn.dianyinhuoban.szg.mvp.gift.contract.GiftContract
import cn.dianyinhuoban.szg.mvp.gift.model.GiftModel
import com.wareroom.lib_base.mvp.BasePresenter
import com.wareroom.lib_http.CustomResourceSubscriber
import com.wareroom.lib_http.exception.ApiException
import com.wareroom.lib_http.response.ResponseTransformer
import com.wareroom.lib_http.schedulers.SchedulerProvider

class GiftPresenter(view: GiftContract.View):BasePresenter<GiftContract.Model,GiftContract.View>(view),GiftContract.Presenter {
    override fun buildModel(): GiftContract.Model {
        return GiftModel()
    }

    override fun fetchGiftInfo() {
        if (!isDestroy) {
            view?.showLoading(false)
        }
        mModel?.let {
            it.fetchGiftInfo()
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .compose(ResponseTransformer.handleResult())
                .subscribeWith(object : CustomResourceSubscriber<GiftInfoBean?>() {
                    override fun onError(exception: ApiException?) {
                        handleError(exception)
                    }

                    override fun onNext(t: GiftInfoBean) {
                        super.onNext(t)
                        if (!isDestroy) {
                            view?.hideLoading()
                            view?.bindGiftInfo(t)
                        }
                    }
                })
        }
    }

    override fun submitGetGift() {
        if (!isDestroy) {
            view?.showLoading(false)
        }
        mModel?.let {
            it.submitGetGift()
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .compose(ResponseTransformer.handleResult())
                .subscribeWith(object : CustomResourceSubscriber<EmptyBean?>() {
                    override fun onError(exception: ApiException?) {
                        handleError(exception)
                    }

                    override fun onNext(t: EmptyBean) {
                        super.onNext(t)
                        if (!isDestroy) {
                            view?.hideLoading()
                            view?.onGetGiftSuccess()
                        }
                    }
                })
        }
    }
}