package cn.dianyinhuoban.szg.mvp.machine.contract

import cn.dianyinhuoban.szg.mvp.bean.TransferRecordBean
import com.wareroom.lib_base.mvp.IView
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

interface TransferRecordContract {
    interface Model {
        fun fetchTransferRecord(page: Int): Observable<Response<List<TransferRecordBean>?>>
    }

    interface Presenter {
        fun fetchTransferRecord(page: Int)
    }

    interface View : IView {
        fun bindTransferRecord(data: List<TransferRecordBean>?)
    }
}