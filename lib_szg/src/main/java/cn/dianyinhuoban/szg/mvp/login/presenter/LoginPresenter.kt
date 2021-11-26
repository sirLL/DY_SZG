package cn.dianyinhuoban.szg.mvp.login.presenter

import cn.dianyinhuoban.szg.mvp.bean.AuthResult
import com.wareroom.lib_http.CustomResourceSubscriber
import cn.dianyinhuoban.szg.mvp.bean.UserBean
import cn.dianyinhuoban.szg.mvp.login.contract.LoginContract
import cn.dianyinhuoban.szg.mvp.login.model.LoginModel
import com.wareroom.lib_base.mvp.BasePresenter
import com.wareroom.lib_base.utils.cache.MMKVUtil
import com.wareroom.lib_http.exception.ApiException
import com.wareroom.lib_http.response.ResponseTransformer
import com.wareroom.lib_http.schedulers.SchedulerProvider

class LoginPresenter(view: LoginContract.View) :
    BasePresenter<LoginModel, LoginContract.View>(view), LoginContract.Presenter {
    override fun buildModel(): LoginModel {
        return LoginModel()
    }

    override fun login(userName: String, password: String) {

        if (!isDestroy) {
            view?.showLoading()
            mModel?.let {
                addDispose(
                    it.submitLogin(userName, password)
                        .compose(SchedulerProvider.getInstance().applySchedulers())
                        .compose(ResponseTransformer.handleResult())
                        .subscribeWith(object : CustomResourceSubscriber<UserBean?>() {
                            override fun onError(exception: ApiException?) {
                                handleError(exception)
                            }

                            override fun onNext(t: UserBean) {
                                super.onNext(t)
                                handLoginInfo(t, password)
                            }
                        })
                )

            }
        }
    }

    private fun handLoginInfo(userBean: UserBean?, password: String) {
        if (!isDestroy) {
            if (userBean != null) {
                if ("1" == userBean.status) {
                    MMKVUtil.saveUserID(userBean.uid)
                    MMKVUtil.saveUserName(userBean.username)
                    MMKVUtil.saveToken(userBean.token)
                    MMKVUtil.savePhone(userBean.phone)
                    MMKVUtil.saveNick(userBean.name)
                    MMKVUtil.saveLoginPassword(password)
                    MMKVUtil.saveInviteCode(userBean.inviteCode)
                    MMKVUtil.saveAvatar(userBean.avatar)
                    fetchAuthResult(userBean.token)
                } else {
                    view?.hideLoading()
                    view?.showMessage("该账号已被冻结")
                }
            } else {
                view?.hideLoading()
                view?.showMessage("获取用户信息失败")
            }
        }
    }

    override fun fetchAuthResult(token: String) {
        if (!isDestroy) {
            view?.showLoading(false)
        }
        mModel?.let {
            addDispose(
                it.fetchAuthResult(token)
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
                                when (t.status) {
                                    "2" -> {
                                        view?.onLoginSuccess()
                                    }
                                    else -> {
                                        view?.showAuthResult(t, token)
                                    }
                                }
                            }
                        }
                    })
            )
        }
    }

}