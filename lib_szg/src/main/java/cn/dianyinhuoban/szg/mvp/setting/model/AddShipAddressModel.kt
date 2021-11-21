package cn.dianyinhuoban.szg.mvp.setting.model

import cn.dianyinhuoban.szg.api.ApiService
import cn.dianyinhuoban.szg.api.ProfileApiService
import cn.dianyinhuoban.szg.mvp.bean.EmptyBean
import cn.dianyinhuoban.szg.mvp.setting.contract.AddShipAddressContract
import com.wareroom.lib_base.mvp.BaseModel
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

class AddShipAddressModel : BaseModel(), AddShipAddressContract.Model {
    override fun submitShipAddress(
        name: String,
        provinceId: String,
        cityId: String,
        districtId: String,
        address: String,
        phone: String,
        id: String
    ): Observable<Response<String?>> {
        return mRetrofit.create(ProfileApiService::class.java)
            .submitShipAddress(name, provinceId, cityId, districtId, address, phone, id)
    }

    override fun deleteAddress(ids: String): Observable<Response<EmptyBean?>> {
        return mRetrofit.create(ApiService::class.java)
            .deleteAddress(ids)
    }
}