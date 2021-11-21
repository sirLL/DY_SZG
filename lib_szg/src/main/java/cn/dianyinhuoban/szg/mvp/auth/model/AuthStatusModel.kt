package cn.dianyinhuoban.szg.mvp.auth.model

import cn.dianyinhuoban.szg.api.ApiService
import cn.dianyinhuoban.szg.mvp.auth.contract.AuthStatusContract
import cn.dianyinhuoban.szg.mvp.bean.AuthResult
import com.wareroom.lib_base.mvp.BaseModel
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

class AuthStatusModel : BaseModel(), AuthStatusContract.Model {
    override fun fetchAuthResult(): Observable<Response<AuthResult?>> {
        return mRetrofit.create(ApiService::class.java)
            .fetchAuthResult()
    }
}