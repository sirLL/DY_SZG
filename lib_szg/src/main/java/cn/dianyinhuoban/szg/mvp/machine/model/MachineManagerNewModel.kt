package cn.dianyinhuoban.szg.mvp.machine.model

import cn.dianyinhuoban.szg.api.ApiService
import cn.dianyinhuoban.szg.mvp.bean.MyMachineBeanNew
import cn.dianyinhuoban.szg.mvp.machine.contract.MachineManagerNewContract
import com.wareroom.lib_base.mvp.BaseModel
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

class MachineManagerNewModel : BaseModel(), MachineManagerNewContract.Model {
    override fun fetchMachine(
        type: String,
        status: String,
        sn: String,
        startDate: String,
        endDate: String,
        page: Int
    ): Observable<Response<MyMachineBeanNew?>> {
        return mRetrofit.create(ApiService::class.java)
            .fetchMyMachineNew(type, status, sn, startDate, endDate, page)
    }
}