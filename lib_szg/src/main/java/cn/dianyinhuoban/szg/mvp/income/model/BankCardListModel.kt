package cn.dianyinhuoban.szg.mvp.income.model

import cn.dianyinhuoban.szg.api.BankApiService
import cn.dianyinhuoban.szg.mvp.bean.BankBean
import cn.dianyinhuoban.szg.mvp.income.contract.BankCardListContract
import com.wareroom.lib_base.mvp.BaseModel
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

class BankCardListModel : BaseModel(), BankCardListContract.Model {
    override fun getBankList(): Observable<Response<List<BankBean>?>> {
        return mRetrofit.create(BankApiService::class.java)
            .getBankList()
    }
}