package cn.dianyinhuoban.szg.mvp.order.model

import cn.dianyinhuoban.szg.api.ApiService
import cn.dianyinhuoban.szg.mvp.bean.AddressBean
import cn.dianyinhuoban.szg.mvp.bean.PayInfoBean
import cn.dianyinhuoban.szg.mvp.order.contract.CreateOrderContract
import com.wareroom.lib_base.mvp.BaseModel
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable
import retrofit2.http.Field

class CreateOrderModel : BaseModel(), CreateOrderContract.Model {
    override fun fetchAddress(page: Int): Observable<Response<List<AddressBean>?>> {
        return mRetrofit.create(ApiService::class.java)
            .fetchAddressList(page)
    }

    override fun submitPurchaseOrder(
        productID: String,
        num: String,
        addressID: String,
        payType: String,
        payName: String,
        bankNo: String,
        bankName: String,
        voucher: String,
        password: String,
    ): Observable<Response<PayInfoBean?>> {
        return mRetrofit.create(ApiService::class.java)
            .submitPurchaseOrder(
                productID,
                num,
                addressID,
                payType,
                payName,
                bankNo,
                bankName,
                voucher,
                password,
              ""
            )
    }
}