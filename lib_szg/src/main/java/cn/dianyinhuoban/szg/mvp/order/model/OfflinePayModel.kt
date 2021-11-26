package cn.dianyinhuoban.szg.mvp.order.model

import cn.dianyinhuoban.szg.api.ApiService
import cn.dianyinhuoban.szg.api.UploadService
import cn.dianyinhuoban.szg.mvp.bean.OfflinePayInfoBean
import cn.dianyinhuoban.szg.mvp.bean.PayInfoBean
import cn.dianyinhuoban.szg.mvp.bean.UploadResultBean
import cn.dianyinhuoban.szg.mvp.order.contract.OfflinePayContract
import com.wareroom.lib_base.mvp.BaseModel
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class OfflinePayModel : BaseModel(), OfflinePayContract.Model {
    override fun fetchOfflinePayInfo(): Observable<Response<OfflinePayInfoBean?>> {
        return mRetrofit.create(ApiService::class.java).fetchOfflinePayInfo()
    }

    override fun uploadVoucher(file: File): Observable<Response<UploadResultBean>> {
        val parse = "multipart/form-data"
        val requestFile: RequestBody = RequestBody.create(MediaType.parse(parse), file)
        val multipartBody: MultipartBody.Part =
            MultipartBody.Part.createFormData("file", file.name, requestFile)
        return mRetrofit.create(UploadService::class.java).upload(multipartBody)
    }

    override fun submitPurchaseOrder(
        productID: String,
        num: String,
        addressID: String,
        payName: String,
        bankNo: String,
        bankName: String,
        voucher: String,
        password: String
    ): Observable<Response<PayInfoBean?>> {
        return mRetrofit.create(ApiService::class.java)
            .submitPurchaseOrder(
                productID,
                num,
                addressID,
                "6",
                payName,
                bankNo,
                bankName,
                voucher,
                password
            )
    }
}