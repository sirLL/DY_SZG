package cn.dianyinhuoban.szg.mvp.machine.model

import cn.dianyinhuoban.szg.api.ApiService
import cn.dianyinhuoban.szg.mvp.bean.MyMachineBean
import cn.dianyinhuoban.szg.mvp.bean.TeamMachineItemBean
import cn.dianyinhuoban.szg.mvp.machine.contract.MyMachineContract
import com.wareroom.lib_base.mvp.BaseModel
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

class MyMachineModel : BaseModel(), MyMachineContract.Model {
    override fun fetchMachine(
        type: String,
        status: String,
        sn: String,
        page: Int
    ): Observable<Response<MyMachineBean?>> {
        return mRetrofit.create(ApiService::class.java).fetchMyMachine(type, status, sn, "", page)
    }

    override fun fetchTeamMachine(
        name: String,
        page: Int
    ): Observable<Response<List<TeamMachineItemBean>?>> {
        return mRetrofit.create(ApiService::class.java)
            .fetchTeamMachine(name, page)
    }
}