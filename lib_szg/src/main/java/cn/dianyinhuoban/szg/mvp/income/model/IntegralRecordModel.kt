package cn.dianyinhuoban.szg.mvp.income.model

import cn.dianyinhuoban.szg.api.ApiService
import cn.dianyinhuoban.szg.bean.IntegralRecordBean
import cn.dianyinhuoban.szg.mvp.income.contract.IntegralRecordContract
import com.wareroom.lib_base.mvp.BaseModel
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

class IntegralRecordModel : BaseModel(), IntegralRecordContract.Model {
    override fun fetchIntegralRecord(
        machineType: String,
        status: String,
        page: Int
    ): Observable<Response<List<IntegralRecordBean>?>> {
        return mRetrofit.create(ApiService::class.java)
            .fetchIntegralRecord(machineType, status, page)
    }
}