package cn.dianyinhuoban.szg.mvp.income.contract

import cn.dianyinhuoban.szg.mvp.bean.IntegralDetailBean
import com.wareroom.lib_base.mvp.IModel
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.mvp.IView
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

interface IntegralDetailContract {
    interface Model : IModel {
        fun fetchIntegralDetail(
            recordID: String
        ): Observable<Response<IntegralDetailBean?>>
    }

    interface Presenter : IPresenter {
        fun fetchIntegralDetail(
            recordID: String
        )
    }

    interface View : IView {
        fun bindIntegralDetail(detailBean: IntegralDetailBean?)
    }
}