package cn.dianyinhuoban.szg.mvp.machine.contract

import cn.dianyinhuoban.szg.mvp.bean.IntegralBalanceBean
import cn.dianyinhuoban.szg.mvp.bean.PurchaseProductBean
import com.wareroom.lib_base.mvp.IModel
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.mvp.IView
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

interface ExchangeContract {
    interface Model : IModel {
        fun fetchPurchaseProduct(
            typeID: String,
            page: Int
        ): Observable<Response<List<PurchaseProductBean>?>>

        fun fetchIntegralBalance(machineTypeId: String): Observable<Response<List<IntegralBalanceBean>?>>
    }

    interface Presenter : IPresenter {
        fun fetchPurchaseProduct(typeID: String, page: Int)

        fun fetchIntegralBalance(machineTypeId: String)
    }

    interface View : IView {
        fun bindPurchaseProduct(productData: List<PurchaseProductBean>?)

        fun bindIntegralBalance(data: List<IntegralBalanceBean>?)
    }
}