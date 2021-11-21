package cn.dianyinhuoban.szg.mvp.machine.contract

import cn.dianyinhuoban.szg.mvp.bean.MachineTypeBean
import com.wareroom.lib_base.mvp.IView
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

interface MachineTypeContract {
    interface Model {
        fun fetchMachineType(): Observable<Response<List<MachineTypeBean>?>>
    }

    interface Presenter {
        fun fetchMachineType()
    }

    interface View : IView {
        fun bindMachineType(data: List<MachineTypeBean>?)
    }
}