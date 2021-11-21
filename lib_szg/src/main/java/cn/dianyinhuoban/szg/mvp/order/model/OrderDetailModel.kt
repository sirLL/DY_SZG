package cn.dianyinhuoban.szg.mvp.order.model

import cn.dianyinhuoban.szg.api.ApiService
import cn.dianyinhuoban.szg.mvp.bean.EmptyBean
import cn.dianyinhuoban.szg.mvp.bean.OrderBean
import cn.dianyinhuoban.szg.mvp.order.contract.OrderDetailContract
import com.wareroom.lib_base.mvp.BaseModel
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

class OrderDetailModel : BaseModel(), OrderDetailContract.Model {
    override fun fetchPurchaseRecord(orderID: String): Observable<Response<List<OrderBean>?>> {
        return mRetrofit.create(ApiService::class.java)
            .fetchPurchaseRecord(orderID)
    }

    override fun submitConfirmReceipt(orderID: String): Observable<Response<EmptyBean?>> {
        return mRetrofit.create(ApiService::class.java)
            .submitConfirmReceipt(orderID)
    }
}