package cn.dianyinhuoban.szg.mvp.machine.model

import cn.dianyinhuoban.szg.api.ApiService
import cn.dianyinhuoban.szg.mvp.bean.MachineTradeRecordBean
import cn.dianyinhuoban.szg.mvp.machine.contract.MachineTradeRecordContract
import com.wareroom.lib_base.mvp.BaseModel
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

class MachineTradeRecordModel : BaseModel(), MachineTradeRecordContract.Model {
    override fun fetchTradeRecord(
        sn: String,
        page: Int
    ): Observable<Response<List<MachineTradeRecordBean>?>> {
        return mRetrofit.create(ApiService::class.java)
            .fetchTradeRecord(sn, page)
    }
}