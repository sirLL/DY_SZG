package cn.dianyinhuoban.szg.mvp.gift.model

import cn.dianyinhuoban.szg.api.ApiService
import cn.dianyinhuoban.szg.bean.GiftInfoBean
import cn.dianyinhuoban.szg.mvp.bean.EmptyBean
import cn.dianyinhuoban.szg.mvp.gift.contract.GiftContract
import com.wareroom.lib_base.mvp.BaseModel
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

class GiftModel : BaseModel(), GiftContract.Model {
    override fun fetchGiftInfo(): Observable<Response<GiftInfoBean?>> {
        return mRetrofit.create(ApiService::class.java).fetchGiftInfo()
    }

    override fun submitGetGift(): Observable<Response<EmptyBean?>> {
        return mRetrofit.create(ApiService::class.java).submitGetGift()
    }
}