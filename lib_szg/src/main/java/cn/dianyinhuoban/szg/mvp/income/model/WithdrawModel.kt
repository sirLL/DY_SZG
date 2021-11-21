package cn.dianyinhuoban.szg.mvp.income.model

import cn.dianyinhuoban.szg.api.ApiService
import cn.dianyinhuoban.szg.api.BankApiService
import cn.dianyinhuoban.szg.mvp.bean.BankBean
import cn.dianyinhuoban.szg.mvp.bean.EmptyBean
import cn.dianyinhuoban.szg.mvp.bean.PersonalBean
import cn.dianyinhuoban.szg.mvp.income.contract.WithdrawContract
import com.wareroom.lib_base.mvp.BaseModel
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

class WithdrawModel : BaseModel(), WithdrawContract.Model {
    override fun fetchPersonalData(): Observable<Response<PersonalBean?>> {
        return mRetrofit.create(ApiService::class.java)
            .fetchPersonalData()
    }

    override fun getBankList(): Observable<Response<List<BankBean>?>> {
        return mRetrofit.create(BankApiService::class.java)
            .getBankList()
    }

    override fun submitWithdraw(
        bankCardID: String,
        amount: String,
        payPassword: String
    ): Observable<Response<EmptyBean?>> {
        return mRetrofit.create(ApiService::class.java)
            .submitWithdraw(bankCardID, amount, payPassword)
    }

    override fun getWithdrawFee(amount: String): Observable<Response<String?>> {
        return mRetrofit.create(ApiService::class.java)
            .fetchWithdrawFee(amount)
    }
}