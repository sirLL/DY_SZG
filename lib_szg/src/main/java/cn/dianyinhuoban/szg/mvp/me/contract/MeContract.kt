package cn.dianyinhuoban.szg.mvp.me.contract

import cn.dianyinhuoban.szg.mvp.bean.PersonalBean
import com.wareroom.lib_base.mvp.IView
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

class MeContract {
    interface Model {
        fun fetchPersonalData(): Observable<Response<PersonalBean?>>
    }

    interface Presenter {
        fun fetchPersonalData()
    }

    interface View : IView {
        fun bindPersonalData(personalBean: PersonalBean?)
    }
}