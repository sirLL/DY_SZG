package cn.dianyinhuoban.szg.mvp.pk.contract

import cn.dianyinhuoban.szg.mvp.bean.PKRecordBean
import com.wareroom.lib_base.mvp.IView
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

interface PKRecordContract {
    interface Model {
        fun fetchPKRecord(
            page: Int
        ): Observable<Response<List<PKRecordBean>?>>
    }

    interface Presenter {
        fun fetchPKRecord(page: Int)
    }

    interface View : IView {
        fun bindPKRecord(data: List<PKRecordBean>?)
    }
}