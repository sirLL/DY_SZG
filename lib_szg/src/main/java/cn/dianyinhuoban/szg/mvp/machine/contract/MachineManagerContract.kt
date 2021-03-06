package cn.dianyinhuoban.szg.mvp.machine.contract

import cn.dianyinhuoban.szg.mvp.bean.MachineTypeBean
import cn.dianyinhuoban.szg.mvp.bean.MyMachineBean
import com.wareroom.lib_base.mvp.IModel
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.mvp.IView
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

interface MachineManagerContract {
    interface Model : IModel {
        fun fetchMachineType(): Observable<Response<List<MachineTypeBean>?>>

        fun fetchMachine(
            type: String,
            status: String,
            sn: String,
            page: Int
        ): Observable<Response<MyMachineBean?>>
    }

    interface Presenter : IPresenter {
        fun fetchMachineType()

        fun fetchMachine(
            type: String,
            status: String,
            sn: String,
            page: Int
        )
    }

    interface View : IView {
        fun bindMachineType(data: List<MachineTypeBean>?)

        fun bindMachine(data: MyMachineBean?)
    }
}