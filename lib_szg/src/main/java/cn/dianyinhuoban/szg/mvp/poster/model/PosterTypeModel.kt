package cn.dianyinhuoban.szg.mvp.poster.model

import cn.dianyinhuoban.szg.api.ApiService
import cn.dianyinhuoban.szg.mvp.bean.PosterTypeBean
import cn.dianyinhuoban.szg.mvp.poster.contract.PosterTypeContract
import com.wareroom.lib_base.mvp.BaseModel
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

class PosterTypeModel : BaseModel(), PosterTypeContract.Model {
    override fun fetchPosterType(): Observable<Response<List<PosterTypeBean>?>> {
        return mRetrofit.create(ApiService::class.java)
            .fetchPosterType()
    }
}