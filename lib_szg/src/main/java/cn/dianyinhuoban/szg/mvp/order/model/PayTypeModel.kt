package cn.dianyinhuoban.szg.mvp.order.model

import cn.dianyinhuoban.szg.api.ApiService
import cn.dianyinhuoban.szg.mvp.bean.IntegralBalanceBean
import cn.dianyinhuoban.szg.mvp.bean.OfflinePayInfoBean
import cn.dianyinhuoban.szg.mvp.order.contract.PayTypeContract
import com.wareroom.lib_base.mvp.BaseModel
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

class PayTypeModel : BaseModel(), PayTypeContract.Model {
    override fun fetchIntegralBalance(machineTypeId: String): Observable<Response<List<IntegralBalanceBean>?>> {
        return mRetrofit.create(ApiService::class.java).fetchIntegralBalance(machineTypeId)
    }

    override fun fetchOfflinePayInfo(): Observable<Response<OfflinePayInfoBean?>> {
        return mRetrofit.create(ApiService::class.java).fetchOfflinePayInfo()
    }

}