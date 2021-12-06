package cn.dianyinhuoban.szg.mvp.home.model

import cn.dianyinhuoban.szg.api.ApiService
import cn.dianyinhuoban.szg.bean.CustomModel
import cn.dianyinhuoban.szg.bean.GiftInfoBean
import cn.dianyinhuoban.szg.mvp.bean.BannerBean
import cn.dianyinhuoban.szg.mvp.bean.HomeDataBean
import cn.dianyinhuoban.szg.mvp.bean.PersonalBean
import cn.dianyinhuoban.szg.mvp.home.contract.HomeContract
import com.wareroom.lib_base.mvp.BaseModel
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

class HomeModel : BaseModel(),
    HomeContract.Model {
    override fun fetchHomeData(): Observable<Response<HomeDataBean?>> {
        return mRetrofit.create(ApiService::class.java)
            .fetchHomeData()
    }

    override fun fetchNoticeList(): Observable<Response<List<CustomModel>?>> {
        return mRetrofit.create(ApiService::class.java)
            .fetchNoticeList()
    }

    override fun fetchDialogBanner(): Observable<Response<List<BannerBean>?>> {
        return mRetrofit.create(ApiService::class.java)
            .fetchDialogBanner()
    }

    override fun fetchBanner(): Observable<Response<List<BannerBean>?>> {
        return mRetrofit.create(ApiService::class.java)
            .fetchBannerList()
    }

    override fun fetchPersonalData(): Observable<Response<PersonalBean?>> {
        return mRetrofit.create(ApiService::class.java).fetchPersonalData()
    }

    override fun fetchGiftInfo(): Observable<Response<GiftInfoBean?>> {
        return mRetrofit.create(ApiService::class.java).fetchGiftInfo()
    }

}