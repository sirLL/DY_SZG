package cn.dianyinhuoban.szg.mvp.income.model

import cn.dianyinhuoban.szg.api.ApiService
import cn.dianyinhuoban.szg.mvp.bean.IncomeRecordDetailBean
import cn.dianyinhuoban.szg.mvp.income.contract.IncomeRecordContract
import com.wareroom.lib_base.mvp.BaseModel
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

class IncomeRecordModel : BaseModel(), IncomeRecordContract.Model {
    override fun fetchIncomeRecordDetail(id: String): Observable<Response<IncomeRecordDetailBean?>> {
        return mRetrofit.create(ApiService::class.java).fetchIncomeRecordDetail(id)
    }
}