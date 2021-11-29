package cn.dianyinhuoban.szg.mvp.me.model

import cn.dianyinhuoban.szg.api.ApiService
import cn.dianyinhuoban.szg.mvp.bean.AuthResult
import cn.dianyinhuoban.szg.mvp.bean.IntegralBalanceBean
import cn.dianyinhuoban.szg.mvp.bean.PersonalBean
import cn.dianyinhuoban.szg.mvp.me.contract.MeContract
import com.wareroom.lib_base.mvp.BaseModel
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

class MeModel : BaseModel(), MeContract.Model {
    override fun fetchPersonalData(): Observable<Response<PersonalBean?>> {
        return mRetrofit.create(ApiService::class.java).fetchPersonalData()
    }

    override fun fetchAuthResult(): Observable<Response<AuthResult?>> {
        return mRetrofit.create(ApiService::class.java)
            .fetchAuthResult()
    }

    override fun fetchIntegralBalance(machineTypeId: String): Observable<Response<List<IntegralBalanceBean>?>> {
        return mRetrofit.create(ApiService::class.java).fetchIntegralBalance(machineTypeId)
    }
}