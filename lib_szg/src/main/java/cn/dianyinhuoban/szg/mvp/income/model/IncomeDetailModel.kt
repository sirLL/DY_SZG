package cn.dianyinhuoban.szg.mvp.income.model

import cn.dianyinhuoban.szg.api.ApiService
import cn.dianyinhuoban.szg.mvp.bean.IncomeDetailBean
import cn.dianyinhuoban.szg.mvp.income.contract.IncomeDetailContract
import com.wareroom.lib_base.mvp.BaseModel
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

class IncomeDetailModel : BaseModel(), IncomeDetailContract.Model {
    override fun fetchIncomeDetail(
        type: String,
        date: String,
        page: Int
    ): Observable<Response<IncomeDetailBean?>> {
        return mRetrofit.create(ApiService::class.java)
            .fetchIncomeDetail(type, date, page)
    }
}