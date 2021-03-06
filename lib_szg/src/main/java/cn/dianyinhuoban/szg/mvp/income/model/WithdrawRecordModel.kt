package cn.dianyinhuoban.szg.mvp.income.model

import cn.dianyinhuoban.szg.api.ApiService
import cn.dianyinhuoban.szg.mvp.bean.WithdrawRecordBean
import cn.dianyinhuoban.szg.mvp.income.contract.WithdrawRecordContract
import com.wareroom.lib_base.mvp.BaseModel
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

class WithdrawRecordModel : BaseModel(), WithdrawRecordContract.Model {
    override fun fetchWithdrawRecord(
        startTime: String,
        endTime: String,
        type: String,
        status: String,
        page: Int
    ): Observable<Response<List<WithdrawRecordBean>?>> {
        return mRetrofit.create(ApiService::class.java)
            .fetchWithdrawRecord(startTime, endTime, type, status, page)
    }
}