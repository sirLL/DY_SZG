package cn.dianyinhuoban.szg.api

import cn.dianyinhuoban.szg.mvp.bean.*
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable
import retrofit2.http.*

interface BankApiService {

    /**
     * 添加银行卡
     */
    @FormUrlEncoded
    @POST(URLConfig.URL_ADD_BANK)
    fun addBank(
        @Field("name") name: String,
        @Field("bankName") bankName: String,
        @Field("bankNo") bankNo: String,
        @Field("phone") phone: String,
        @Field("code") code: String,
    ): Observable<Response<EmptyBean?>>


    /**
     * 修改银行卡
     */
    @FormUrlEncoded
    @POST(URLConfig.URL_ADD_BANK)
    fun updateBank(
        @Field("name") name: String,
        @Field("bankName") bankName: String,
        @Field("bankNo") bankNo: String,
        @Field("phone") phone: String,
        @Field("code") code: String,
        @Field("id") id: String,
    ): Observable<Response<EmptyBean?>>

    @POST(URLConfig.URL_BANK_LIST)
    fun getBankList(): Observable<Response<List<BankBean>?>>

    @FormUrlEncoded
    @POST(URLConfig.URL_BANK_SET)
    fun setBank(   @Field("type") type: String,
                   @Field("id") id: String): Observable<Response<EmptyBean?>>





}