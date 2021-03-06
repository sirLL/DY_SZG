package cn.dianyinhuoban.szg.mvp.income.contract

import cn.dianyinhuoban.szg.mvp.bean.BankBean
import com.wareroom.lib_base.mvp.IView
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

interface BankCardListContract {
    interface Model {
        fun getBankList(): Observable<Response<List<BankBean>?>>
    }

    interface Presenter {
        fun getBankList()
    }

    interface View : IView {
        fun onLoadBankList(bankBeanList: List<BankBean>)
    }

}