package cn.dianyinhuoban.szg.mvp.order.model

import cn.dianyinhuoban.szg.api.ApiService
import cn.dianyinhuoban.szg.mvp.bean.PurchaseProductBean
import cn.dianyinhuoban.szg.mvp.order.contract.ProductListContract
import com.wareroom.lib_base.mvp.BaseModel
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

class ProductListModel : BaseModel(), ProductListContract.Model {
    override fun fetchPurchaseProduct(
        typeID: String,
        page: Int
    ): Observable<Response<List<PurchaseProductBean>?>> {
        return mRetrofit.create(ApiService::class.java)
            .fetchPurchaseProduct(typeID, page)
    }
}