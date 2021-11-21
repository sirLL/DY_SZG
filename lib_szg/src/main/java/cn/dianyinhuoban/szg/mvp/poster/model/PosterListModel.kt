package cn.dianyinhuoban.szg.mvp.poster.model

import cn.dianyinhuoban.szg.api.ApiService
import cn.dianyinhuoban.szg.mvp.bean.PosterItemBean
import cn.dianyinhuoban.szg.mvp.poster.contract.PosterListContract
import com.wareroom.lib_base.mvp.BaseModel
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

class PosterListModel : BaseModel(), PosterListContract.Model {
    override fun fetchPosterList(
        typeID: String,
        page: Int
    ): Observable<Response<List<PosterItemBean>?>> {
        return mRetrofit.create(ApiService::class.java)
            .fetchPosterList(typeID, page)
    }
}