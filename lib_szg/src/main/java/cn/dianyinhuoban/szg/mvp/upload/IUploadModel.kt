package cn.dianyinhuoban.szg.mvp.upload

import cn.dianyinhuoban.szg.mvp.bean.UploadResultBean
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable
import java.io.File

interface IUploadModel {

    fun upload(file: File): Observable<Response<UploadResultBean>>


}