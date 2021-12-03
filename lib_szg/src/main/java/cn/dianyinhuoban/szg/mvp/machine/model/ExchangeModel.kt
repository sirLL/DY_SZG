package cn.dianyinhuoban.szg.mvp.machine.model

import cn.dianyinhuoban.szg.api.ApiService
import cn.dianyinhuoban.szg.mvp.bean.IntegralBalanceBean
import cn.dianyinhuoban.szg.mvp.bean.PurchaseProductBean
import cn.dianyinhuoban.szg.mvp.machine.contract.ExchangeContract
import com.wareroom.lib_base.mvp.BaseModel
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

class ExchangeModel : BaseModel(), ExchangeContract.Model {
    override fun fetchPurchaseProduct(
        typeID: String,
        page: Int
    ): Observable<Response<List<PurchaseProductBean>?>> {
        return mRetrofit.create(ApiService::class.java)
            .fetchPurchaseProduct(typeID, page)
    }

    override fun fetchIntegralBalance(machineTypeId: String): Observable<Response<List<IntegralBalanceBean>?>> {
        return mRetrofit.create(ApiService::class.java).fetchIntegralBalance(machineTypeId)
    }
}