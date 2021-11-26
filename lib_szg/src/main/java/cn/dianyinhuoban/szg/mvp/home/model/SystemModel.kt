package cn.dianyinhuoban.szg.mvp.home.model

import cn.dianyinhuoban.szg.api.ApiService
import cn.dianyinhuoban.szg.mvp.bean.AuthResult
import cn.dianyinhuoban.szg.mvp.bean.SystemItemBean
import cn.dianyinhuoban.szg.mvp.home.contract.SystemContract
import com.wareroom.lib_base.mvp.BaseModel
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

class SystemModel : BaseModel(), SystemContract.Model {
    override fun fetchSystemSetting(): Observable<Response<List<SystemItemBean>?>> {
        return mRetrofit.create(ApiService::class.java)
            .fetchSystemSetting()
    }

    override fun fetchAuthResult(): Observable<Response<AuthResult?>> {
        return mRetrofit.create(ApiService::class.java)
            .fetchAuthResult()
    }

}