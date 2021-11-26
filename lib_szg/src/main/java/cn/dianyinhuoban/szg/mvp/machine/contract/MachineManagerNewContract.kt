package cn.dianyinhuoban.szg.mvp.machine.contract

import cn.dianyinhuoban.szg.mvp.bean.MyMachineBeanNew
import com.wareroom.lib_base.mvp.IModel
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.mvp.IView
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

interface MachineManagerNewContract {
    interface Model : IModel {
        fun fetchMachine(
            type: String,
            status: String,
            sn: String,
            startDate: String,
            endDate: String,
            page: Int
        ): Observable<Response<MyMachineBeanNew?>>
    }

    interface Presenter : IPresenter {

        fun fetchMachine(
            type: String,
            status: String,
            sn: String,
            startDate: String,
            endDate: String,
            page: Int
        )
    }

    interface View : IView {
        fun bindMachine(data: MyMachineBeanNew?)
    }
}