package cn.dianyinhuoban.szg.mvp.order.contract

import cn.dianyinhuoban.szg.mvp.bean.AddressBean
import cn.dianyinhuoban.szg.mvp.bean.PayInfoBean
import com.wareroom.lib_base.mvp.IView
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

interface CreateOrderContract {
    interface Model {
        fun fetchAddress(
            page: Int,
        ): Observable<Response<List<AddressBean>?>>

        fun submitPurchaseOrder(
            productID: String,
            num: String,
            addressID: String,
            payType: String,
            payName: String,
            bankNo: String,
            bankName: String,
            voucher: String,
            password: String
        ): Observable<Response<PayInfoBean?>>
    }

    interface Presenter {
        fun fetchAddress()

        fun submitPurchaseOrder(
            productID: String,
            num: String,
            addressID: String,
            payType: String,
            payName: String,
            bankNo: String,
            bankName: String,
            voucher: String,
            password: String
        )
    }

    interface View : IView {
        fun startAlipay(payInfo: String)
        fun startWechatPay()
        fun onSubmitOrderSuccess()

        fun bindDefAddress(addressData: List<AddressBean?>?)
    }
}