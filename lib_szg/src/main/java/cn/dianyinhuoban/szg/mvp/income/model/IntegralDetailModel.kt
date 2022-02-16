package cn.dianyinhuoban.szg.mvp.income.model

import cn.dianyinhuoban.szg.api.ApiService
import cn.dianyinhuoban.szg.mvp.bean.IntegralDetailBean
import cn.dianyinhuoban.szg.mvp.income.contract.IntegralDetailContract
import com.wareroom.lib_base.mvp.BaseModel
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

class IntegralDetailModel : BaseModel(), IntegralDetailContract.Model {
    override fun fetchIntegralDetail(recordID: String): Observable<Response<IntegralDetailBean?>> {
        return mRetrofit.create(ApiService::class.java).fetchIntegralRecordDetail(recordID)
    }
}