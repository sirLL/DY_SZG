package cn.dianyinhuoban.szg.mvp.me.model

import cn.dianyinhuoban.szg.api.ApiService
import cn.dianyinhuoban.szg.bean.MemberLevelBean
import cn.dianyinhuoban.szg.mvp.me.contract.TeamGroupContract
import com.wareroom.lib_base.mvp.BaseModel
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

class TeamGroupModel : BaseModel(), TeamGroupContract.Model {
    override fun fetchMemberLevelList(): Observable<Response<List<MemberLevelBean?>?>> {
        return mRetrofit.create(ApiService::class.java).fetchMemberLevelList()
    }
}