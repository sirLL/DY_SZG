package cn.dianyinhuoban.szg.mvp.order.contract

import cn.dianyinhuoban.szg.mvp.bean.IntegralBalanceBean
import cn.dianyinhuoban.szg.mvp.bean.OfflinePayInfoBean
import com.wareroom.lib_base.mvp.IModel
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.mvp.IView
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

interface PayTypeContract {
    interface Model : IModel {
        fun fetchIntegralBalance(machineTypeId: String): Observable<Response<List<IntegralBalanceBean>?>>

        fun fetchOfflinePayInfo(): Observable<Response<OfflinePayInfoBean?>>
    }

    interface Presenter :IPresenter{
        fun fetchIntegralBalance(machineTypeId: String)

        fun fetchOfflinePayInfo()
    }

    interface View:IView{
        fun bindIntegralBalance(data: List<IntegralBalanceBean>?)

        fun bindOfflinePayInfo(payInfoBean: OfflinePayInfoBean?)
    }
}