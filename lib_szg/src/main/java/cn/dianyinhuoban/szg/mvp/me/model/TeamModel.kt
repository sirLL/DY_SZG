package cn.dianyinhuoban.szg.mvp.me.model

import cn.dianyinhuoban.szg.api.ApiService
import cn.dianyinhuoban.szg.mvp.bean.TeamInfoBean
import cn.dianyinhuoban.szg.mvp.me.contract.TeamContract
import com.wareroom.lib_base.mvp.BaseModel
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

class TeamModel : BaseModel(), TeamContract.Model {
    override fun fetchMyTeam(trans: String, regTime: String, groupId: String, page: Int, isAuth: String): Observable<Response<TeamInfoBean?>> {
        return mRetrofit.create(ApiService::class.java).fetchMyTeam(trans, regTime, groupId, page, isAuth)
    }
}