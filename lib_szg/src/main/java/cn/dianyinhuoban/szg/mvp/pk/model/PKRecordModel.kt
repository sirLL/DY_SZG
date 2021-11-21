package cn.dianyinhuoban.szg.mvp.pk.model

import cn.dianyinhuoban.szg.api.PKApiService
import cn.dianyinhuoban.szg.mvp.bean.PKRecordBean
import cn.dianyinhuoban.szg.mvp.pk.contract.PKRecordContract
import com.wareroom.lib_base.mvp.BaseModel
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

class PKRecordModel : BaseModel(), PKRecordContract.Model {
    override fun fetchPKRecord(page: Int): Observable<Response<List<PKRecordBean>?>> {
        return mRetrofit.create(PKApiService::class.java)
            .fetchPKRecord(page)
    }
}