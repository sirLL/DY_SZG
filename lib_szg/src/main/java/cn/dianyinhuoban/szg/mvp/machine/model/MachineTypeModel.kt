package cn.dianyinhuoban.szg.mvp.machine.model

import cn.dianyinhuoban.szg.api.ApiService
import cn.dianyinhuoban.szg.mvp.bean.MachineTypeBean
import cn.dianyinhuoban.szg.mvp.machine.contract.MachineTypeContract
import com.wareroom.lib_base.mvp.BaseModel
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

class MachineTypeModel :BaseModel(),MachineTypeContract.Model {
    override fun fetchMachineType(): Observable<Response<List<MachineTypeBean>?>> {
        return mRetrofit.create(ApiService::class.java)
            .fetchMachineType()
    }
}