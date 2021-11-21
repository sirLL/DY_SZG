package cn.dianyinhuoban.szg.api

import cn.dianyinhuoban.szg.mvp.bean.*
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.http.*

interface UploadService {

    @Multipart
    @POST(URLConfig.URL_UPLOAD_FILE)
    fun upload( @Part file: MultipartBody.Part): Observable<Response<UploadResultBean>>

}