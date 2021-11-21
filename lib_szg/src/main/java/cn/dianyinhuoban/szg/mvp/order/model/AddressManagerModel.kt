package cn.dianyinhuoban.szg.mvp.order.model

import cn.dianyinhuoban.szg.api.ApiService
import cn.dianyinhuoban.szg.mvp.bean.AddressBean
import cn.dianyinhuoban.szg.mvp.order.contract.AddressManagerContract
import com.wareroom.lib_base.mvp.BaseModel
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

class AddressManagerModel : BaseModel(), AddressManagerContract.Model {
    override fun fetchAddressList(page: Int): Observable<Response<List<AddressBean>?>> {
        return mRetrofit.create(ApiService::class.java)
            .fetchAddressList(page)
    }
}