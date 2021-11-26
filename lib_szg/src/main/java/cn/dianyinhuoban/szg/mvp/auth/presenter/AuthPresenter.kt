package cn.dianyinhuoban.szg.mvp.auth.presenter

import com.wareroom.lib_http.CustomResourceSubscriber
import cn.dianyinhuoban.szg.mvp.auth.contract.AuthContract
import cn.dianyinhuoban.szg.mvp.auth.model.AuthModel
import cn.dianyinhuoban.szg.mvp.bean.EmptyBean
import com.wareroom.lib_base.mvp.BasePresenter
import com.wareroom.lib_http.exception.ApiException
import com.wareroom.lib_http.response.ResponseTransformer
import com.wareroom.lib_http.schedulers.SchedulerProvider

class AuthPresenter(view: AuthContract.View) : BasePresenter<AuthModel, AuthContract.View>(view),
    AuthContract.Presenter {
    override fun buildModel(): AuthModel {
        return AuthModel()
    }

    override fun submitAuth(
        token: String,
        name: String,
        idCard: String,
        positiveURL: String,
        negativeURL: String
    ) {
        mModel?.let {
            if (!isDestroy) {
                view?.showLoading()
            }
            addDispose(
                it.submitAuth(token, name, idCard, positiveURL, negativeURL)
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
                                view?.onSubmitAuthSuccess()
                            }
                        }
                    })
            )
        }
    }
}