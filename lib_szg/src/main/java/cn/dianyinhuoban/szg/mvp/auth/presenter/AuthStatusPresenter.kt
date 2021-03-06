package cn.dianyinhuoban.szg.mvp.auth.presenter

import com.wareroom.lib_http.CustomResourceSubscriber
import cn.dianyinhuoban.szg.mvp.auth.contract.AuthStatusContract
import cn.dianyinhuoban.szg.mvp.auth.model.AuthStatusModel
import cn.dianyinhuoban.szg.mvp.bean.AuthResult
import com.wareroom.lib_base.mvp.BasePresenter
import com.wareroom.lib_http.exception.ApiException
import com.wareroom.lib_http.response.ResponseTransformer
import com.wareroom.lib_http.schedulers.SchedulerProvider

class AuthStatusPresenter(view: AuthStatusContract.View) :
    BasePresenter<AuthStatusModel, AuthStatusContract.View>(view), AuthStatusContract.Presenter {
    override fun buildModel(): AuthStatusModel {
        return AuthStatusModel()
    }

    override fun fetchAuthResult() {
        if (!isDestroy) {
            view?.showLoading(false)
        }
        mModel?.let {
            addDispose(
                it.fetchAuthResult()
                    .compose(SchedulerProvider.getInstance().applySchedulers())
                    .compose(ResponseTransformer.handleResult())
                    .subscribeWith(object : CustomResourceSubscriber<AuthResult?>() {
                        override fun onError(exception: ApiException?) {
                            if (!isDestroy) {
                                view?.hideLoading()
                                handleError(exception)
                            }
                        }

                        override fun onNext(t: AuthResult) {
                            super.onNext(t)
                            if (!isDestroy) {
                                view?.hideLoading()
                                view?.bindAuthResult(t)
                            }
                        }
                    })
            )
        }
    }
}