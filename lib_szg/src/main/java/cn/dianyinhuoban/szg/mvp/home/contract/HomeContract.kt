package cn.dianyinhuoban.szg.mvp.home.contract

import cn.dianyinhuoban.szg.bean.CustomModel
import cn.dianyinhuoban.szg.bean.GiftInfoBean
import cn.dianyinhuoban.szg.mvp.bean.AuthResult
import cn.dianyinhuoban.szg.mvp.bean.BannerBean
import cn.dianyinhuoban.szg.mvp.bean.HomeDataBean
import cn.dianyinhuoban.szg.mvp.bean.PersonalBean
import com.wareroom.lib_base.mvp.IView
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

interface HomeContract {
    interface Model {
        fun fetchHomeData(): Observable<Response<HomeDataBean?>>

        fun fetchNoticeList(): Observable<Response<List<CustomModel>?>>

        fun fetchDialogBanner(): Observable<Response<List<BannerBean>?>>

        fun fetchBanner(): Observable<Response<List<BannerBean>?>>

        fun fetchPersonalData(): Observable<Response<PersonalBean?>>

        fun fetchGiftInfo(): Observable<Response<GiftInfoBean?>>

        fun fetchAuthResult(): Observable<Response<AuthResult?>>
    }

    interface Presenter {
        fun fetchHomeData()

        fun fetchNoticeList()

        fun fetchDialogBanner()

        fun fetchBanner()

        fun fetchPersonalData()

        fun fetchGiftInfo()

        fun fetchAuthResult()
    }

    interface View : IView {
        fun bindHomeData(homeDataBean: HomeDataBean?)

        fun bindNoticeList(data: List<CustomModel>?)

        fun bindDialogBanner(data: List<BannerBean>?)

        fun bindBanner(data: List<BannerBean>?)

        fun bindPersonalData(personalBean: PersonalBean?)

        fun bindGiftInfo(data: GiftInfoBean)

        fun bindAuthResult(authResult: AuthResult)
    }
}