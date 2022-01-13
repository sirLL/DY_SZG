package cn.dianyinhuoban.szg.mvp.order.contract

import cn.dianyinhuoban.szg.mvp.bean.OfflinePayInfoBean
import cn.dianyinhuoban.szg.mvp.bean.PayInfoBean
import cn.dianyinhuoban.szg.mvp.bean.UploadResultBean
import com.wareroom.lib_base.mvp.IModel
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.mvp.IView
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable
import java.io.File

interface OfflinePayContract {
    interface Model : IModel {
        fun fetchOfflinePayInfo(): Observable<Response<OfflinePayInfoBean?>>

        fun uploadVoucher(file: File): Observable<Response<UploadResultBean>>

        fun submitPurchaseOrder(
            productID: String,
            num: String,
            addressID: String,
            payName: String,
            bankNo: String,
            bankName: String,
            voucher: String,
            password: String,
            payMethod: String
        ): Observable<Response<PayInfoBean?>>
    }

    interface Presenter : IPresenter {
        fun fetchOfflinePayInfo()

        fun submitPurchaseOrder(
            productID: String,
            num: String,
            addressID: String,
            payName: String,
            bankNo: String,
            bankName: String,
            voucher: String,
            password: String,
            payMethod: String
        )

        fun uploadVoucher(file: File)
    }

    interface View : IView {
        fun bindOfflinePayInfo(payInfoBean: OfflinePayInfoBean?)

        fun bindVoucher(resultBean: UploadResultBean)

        fun onSubmitOrderSuccess()
    }
}