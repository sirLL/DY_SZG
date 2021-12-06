package cn.dianyinhuoban.szg.mvp.gift.contract

import cn.dianyinhuoban.szg.bean.GiftInfoBean
import cn.dianyinhuoban.szg.mvp.bean.EmptyBean
import com.wareroom.lib_base.mvp.IModel
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.mvp.IView
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

interface GiftContract {
    interface Model : IModel {
        fun fetchGiftInfo(): Observable<Response<GiftInfoBean?>>

        fun submitGetGift(): Observable<Response<EmptyBean?>>
    }

    interface Presenter : IPresenter {
        fun fetchGiftInfo()

        fun submitGetGift()
    }

    interface View : IView {
        fun bindGiftInfo(data: GiftInfoBean)

        fun onGetGiftSuccess()
    }
}