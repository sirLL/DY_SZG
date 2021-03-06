package cn.dianyinhuoban.szg.mvp.home.contract

import cn.dianyinhuoban.szg.mvp.bean.ShareItemBean
import com.wareroom.lib_base.mvp.IView
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

interface ShareContract {
    interface Model {
        fun fetchShareImage(): Observable<Response<List<ShareItemBean>?>>
    }

    interface Presenter {
        fun fetchShareImage()
    }

    interface View : IView {
        fun bindShareImage(data: List<ShareItemBean>?)
    }
}