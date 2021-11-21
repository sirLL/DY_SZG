package cn.dianyinhuoban.szg.mvp.order.model

import cn.dianyinhuoban.szg.api.ApiService
import cn.dianyinhuoban.szg.mvp.bean.OrderBean
import cn.dianyinhuoban.szg.mvp.order.contract.OrderListContract
import com.wareroom.lib_base.mvp.BaseModel
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

class OrderListModel : BaseModel(), OrderListContract.Model {
    override fun fetchPurchaseRecordList(
        status: String,
        page: Int
    ): Observable<Response<List<OrderBean>?>> {
        return mRetrofit.create(ApiService::class.java)
            .fetchPurchaseRecordList(status, page)
    }
}