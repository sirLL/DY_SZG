package cn.dianyinhuoban.szg.mvp.machine.contract

import cn.dianyinhuoban.szg.mvp.bean.EmptyBean
import com.wareroom.lib_base.mvp.IView
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

interface TransferContract {
    interface Model {
        fun submitMachineTransfer(
            receiverUID: String,
            machineType: String,
            isPay: String,
            price: String,
            machineIds: String,
            transferType: String,
            startMachineSN: String,
            endMachineSN: String,
            transPoint:Boolean
        ): Observable<Response<EmptyBean?>>
    }

    interface Presenter {
        fun submitMachineTransfer(
            receiverUID: String,
            machineType: String,
            isPay: String,
            price: String,
            machineIds: String,
            transferType: String,
            startMachineSN: String,
            endMachineSN: String,
            transPoint:Boolean
        )
    }

    interface View : IView {
        fun onSubmitMachineTransferSuccess()
    }
}