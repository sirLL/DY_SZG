package cn.dianyinhuoban.szg.mvp.auth.model

import cn.dianyinhuoban.szg.api.ApiService
import cn.dianyinhuoban.szg.mvp.auth.contract.AuthContract
import cn.dianyinhuoban.szg.mvp.bean.EmptyBean
import com.wareroom.lib_base.mvp.BaseModel
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

class AuthModel : BaseModel(), AuthContract.Model {
    override fun submitAuth(
        name: String,
        idCard: String,
        positiveURL: String,
        negativeURL: String
    ): Observable<Response<EmptyBean?>> {
        return mRetrofit.create(ApiService::class.java)
            .submitAuth(name, idCard, positiveURL, negativeURL)
    }
}