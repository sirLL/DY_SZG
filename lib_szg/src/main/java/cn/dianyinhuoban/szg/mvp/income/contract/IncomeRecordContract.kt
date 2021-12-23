package cn.dianyinhuoban.szg.mvp.income.contract

import cn.dianyinhuoban.szg.mvp.bean.IncomeRecordDetailBean
import com.wareroom.lib_base.mvp.IModel
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.mvp.IView
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

interface IncomeRecordContract {
    interface Model : IModel {
        fun fetchIncomeRecordDetail(id: String): Observable<Response<IncomeRecordDetailBean?>>
    }

    interface Presenter : IPresenter {
        fun fetchIncomeRecordDetail(id: String)
    }

    interface View : IView {
        fun bindIncomeRecordDetail(bean: IncomeRecordDetailBean?)
    }
}