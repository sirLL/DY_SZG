package cn.dianyinhuoban.szg.mvp.auth.contract

import cn.dianyinhuoban.szg.mvp.bean.EmptyBean
import com.wareroom.lib_base.mvp.IView
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

interface AuthContract {
    interface Model {
        fun submitAuth(
            token:String,
            name: String,
            idCard: String,
            positiveURL: String,
            negativeURL: String
        ): Observable<Response<EmptyBean?>>
    }

    interface Presenter {
        fun submitAuth(
            token:String,
            name: String,
            idCard: String,
            positiveURL: String,
            negativeURL: String
        )
    }

    interface View : IView {
        fun onSubmitAuthSuccess()
    }
}