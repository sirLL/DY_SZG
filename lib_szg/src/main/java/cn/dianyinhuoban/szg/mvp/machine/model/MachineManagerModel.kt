package cn.dianyinhuoban.szg.mvp.machine.model

import cn.dianyinhuoban.szg.api.ApiService
import cn.dianyinhuoban.szg.mvp.bean.MachineTypeBean
import cn.dianyinhuoban.szg.mvp.bean.MyMachineBean
import cn.dianyinhuoban.szg.mvp.machine.contract.MachineManagerContract
import com.wareroom.lib_base.mvp.BaseModel
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

class MachineManagerModel : BaseModel(), MachineManagerContract.Model {
    override fun fetchMachineType(): Observable<Response<List<MachineTypeBean>?>> {
        return mRetrofit.create(ApiService::class.java)
            .fetchMachineType()
    }

    override fun fetchMachine(
        type: String,
        status: String,
        sn: String,
        page: Int
    ): Observable<Response<MyMachineBean?>> {
        return mRetrofit.create(ApiService::class.java)
            .fetchMyMachine(type, status, sn,"", page)
    }
}