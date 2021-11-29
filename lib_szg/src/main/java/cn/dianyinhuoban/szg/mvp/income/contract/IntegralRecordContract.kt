package cn.dianyinhuoban.szg.mvp.income.contract

import cn.dianyinhuoban.szg.bean.IntegralRecordBean
import com.wareroom.lib_base.mvp.IModel
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.mvp.IView
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable


class IntegralRecordContract {
    interface Model : IModel {
        fun fetchIntegralRecord(
            machineType: String,
            status: String,
            page: Int
        ): Observable<Response<List<IntegralRecordBean>?>>
    }

    interface Presenter : IPresenter {
        fun fetchIntegralRecord(
            machineType: String,
            status: String,
            page: Int
        )
    }

    interface View : IView {
        fun bindIntegralRecord(data: List<IntegralRecordBean>?)
    }
}