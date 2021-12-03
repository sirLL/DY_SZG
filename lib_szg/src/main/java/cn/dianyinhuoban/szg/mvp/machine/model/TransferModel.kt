package cn.dianyinhuoban.szg.mvp.machine.model

import cn.dianyinhuoban.szg.api.ApiService
import cn.dianyinhuoban.szg.mvp.bean.EmptyBean
import cn.dianyinhuoban.szg.mvp.machine.contract.TransferContract
import com.wareroom.lib_base.mvp.BaseModel
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

class TransferModel : BaseModel(), TransferContract.Model {
    override fun submitMachineTransfer(
        receiverUID: String,
        machineType: String,
        isPay: String,
        price: String,
        machineIds: String,
        transferType: String,
        startMachineSN: String,
        endMachineSN: String,
        transPoint: Boolean
    ): Observable<Response<EmptyBean?>> {
        return mRetrofit.create(ApiService::class.java)
            .submitMachineTransfer(
                receiverUID,
                machineType,
                isPay,
                price,
                machineIds,
                transferType,
                startMachineSN,
                endMachineSN,
                if (transPoint) {
                    "1"
                } else {
                    "0"
                }
            )
    }
}