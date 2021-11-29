package cn.dianyinhuoban.szg.mvp.me.contract

import cn.dianyinhuoban.szg.mvp.bean.AuthResult
import cn.dianyinhuoban.szg.mvp.bean.IntegralBalanceBean
import cn.dianyinhuoban.szg.mvp.bean.PersonalBean
import com.wareroom.lib_base.mvp.IView
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

class MeContract {
    interface Model {
        fun fetchPersonalData(): Observable<Response<PersonalBean?>>

        fun fetchAuthResult(): Observable<Response<AuthResult?>>

        fun fetchIntegralBalance(machineTypeId: String): Observable<Response<List<IntegralBalanceBean>?>>
    }

    interface Presenter {
        fun fetchPersonalData()

        fun fetchAuthResult()

        fun fetchIntegralBalance(machineTypeId: String)
    }

    interface View : IView {
        fun bindPersonalData(personalBean: PersonalBean?)

        fun bindAuthResult(authResult: AuthResult)

        fun bindIntegralBalance(data: List<IntegralBalanceBean>?)
    }
}