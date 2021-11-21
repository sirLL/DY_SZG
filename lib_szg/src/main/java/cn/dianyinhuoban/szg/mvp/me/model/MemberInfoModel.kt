package cn.dianyinhuoban.szg.mvp.me.model

import cn.dianyinhuoban.szg.api.ApiService
import cn.dianyinhuoban.szg.mvp.bean.MemberDetailBean
import cn.dianyinhuoban.szg.mvp.me.contract.MemberInfoContract
import com.wareroom.lib_base.mvp.BaseModel
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

class MemberInfoModel : BaseModel(), MemberInfoContract.Model {
    override fun fetchMemberDetail(
        memberID: String,
        month: String,
    ): Observable<Response<MemberDetailBean?>> {
        return mRetrofit.create(ApiService::class.java)
            .fetchMemberDetail(memberID, month)
    }
}