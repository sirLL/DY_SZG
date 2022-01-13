package cn.dianyinhuoban.szg.mvp.home.presenter

import com.wareroom.lib_http.CustomResourceSubscriber
import cn.dianyinhuoban.szg.bean.CustomModel
import cn.dianyinhuoban.szg.bean.GiftInfoBean
import cn.dianyinhuoban.szg.mvp.bean.AuthResult
import cn.dianyinhuoban.szg.mvp.bean.BannerBean
import cn.dianyinhuoban.szg.mvp.bean.HomeDataBean
import cn.dianyinhuoban.szg.mvp.bean.PersonalBean
import cn.dianyinhuoban.szg.mvp.home.contract.HomeContract
import cn.dianyinhuoban.szg.mvp.home.model.HomeModel
import com.wareroom.lib_base.mvp.BasePresenter
import com.wareroom.lib_base.utils.cache.MMKVUtil
import com.wareroom.lib_http.exception.ApiException
import com.wareroom.lib_http.response.ResponseTransformer
import com.wareroom.lib_http.schedulers.SchedulerProvider

class HomePresenter(view: HomeContract.View) : BasePresenter<HomeModel, HomeContract.View>(view),
    HomeContract.Presenter {
    override fun buildModel(): HomeModel {
        return HomeModel()
    }

    override fun fetchHomeData() {
        mModel?.let {
            addDispose(
                it.fetchHomeData().compose(SchedulerProvider.getInstance().applySchedulers())
                    .compose(ResponseTransformer.handleResult())
                    .subscribeWith(object : CustomResourceSubscriber<HomeDataBean?>() {
                        override fun onError(exception: ApiException?) {
                            handleError(exception)
                        }

                        override fun onNext(t: HomeDataBean) {
                            super.onNext(t)
                            if (!isDestroy) {
                                view?.bindHomeData(t)
                            }
                        }
                    })
            )
        }
    }

    override fun fetchNoticeList() {
        mModel?.let {
            addDispose(
                it.fetchNoticeList()
                    .compose(SchedulerProvider.getInstance().applySchedulers())
                    .compose(ResponseTransformer.handleResult())
                    .subscribeWith(object : CustomResourceSubscriber<List<CustomModel>?>() {
                        override fun onError(exception: ApiException?) {
                            handleError(exception)
                        }

                        override fun onNext(t: List<CustomModel>) {
                            super.onNext(t)
                            if (!isDestroy) {
                                view?.bindNoticeList(t)
                            }
                        }
                    })
            )
        }
    }

    override fun fetchDialogBanner() {
        mModel?.let {
            addDispose(
                it.fetchDialogBanner()
                    .compose(SchedulerProvider.getInstance().applySchedulers())
                    .compose(ResponseTransformer.handleResult())
                    .subscribeWith(object : CustomResourceSubscriber<List<BannerBean>?>() {
                        override fun onError(exception: ApiException?) {
                            handleError(exception)
                        }

                        override fun onNext(data: List<BannerBean>) {
                            super.onNext(data)
                            if (data.isNotEmpty()) {
                                if (!isDestroy) {
                                    view?.bindDialogBanner(data)
                                }
                            }
                        }
                    })
            )
        }
    }

    override fun fetchBanner() {
        mModel?.let {
            addDispose(
                it.fetchBanner()
                    .compose(SchedulerProvider.getInstance().applySchedulers())
                    .compose(ResponseTransformer.handleResult())
                    .subscribeWith(object : CustomResourceSubscriber<List<BannerBean>?>() {
                        override fun onError(exception: ApiException?) {
                            handleError(exception)
                        }

                        override fun onNext(data: List<BannerBean>) {
                            super.onNext(data)
                            if (!isDestroy) {
                                view?.bindBanner(data)
                            }
                        }
                    })
            )
        }
    }

    override fun fetchPersonalData() {
        mModel?.let {
            it.fetchPersonalData()
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .compose(ResponseTransformer.handleResult())
                .subscribeWith(object : CustomResourceSubscriber<PersonalBean?>() {
                    override fun onError(exception: ApiException?) {
                        handleError(exception)
                    }

                    override fun onNext(t: PersonalBean) {
                        super.onNext(t)
                        MMKVUtil.saveUserID(t.uid)
                        MMKVUtil.saveUserName(t.username)
                        MMKVUtil.saveNick(t.name)
                        MMKVUtil.saveAvatar(t.avatar)
                        MMKVUtil.saveTeam(t.teamName)
                        if (!isDestroy) {
                            view?.bindPersonalData(t)
                        }
                    }
                })
        }
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

    override fun fetchAuthResult() {
        mModel?.let {
            addDispose(
                it.fetchAuthResult()
                    .compose(SchedulerProvider.getInstance().applySchedulers())
                    .compose(ResponseTransformer.handleResult())
                    .subscribeWith(object : CustomResourceSubscriber<AuthResult?>() {
                        override fun onError(exception: ApiException?) {
                            if (!isDestroy) {
                                handleError(exception)
                            }
                        }

                        override fun onNext(t: AuthResult) {
                            super.onNext(t)
                            if (!isDestroy) {
                                view?.bindAuthResult(t)
                            }
                        }
                    })
            )
        }
    }
}