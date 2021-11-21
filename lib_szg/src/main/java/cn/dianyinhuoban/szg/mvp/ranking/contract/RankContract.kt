package cn.dianyinhuoban.szg.mvp.ranking.contract

import cn.dianyinhuoban.szg.mvp.bean.RankBean
import com.wareroom.lib_base.mvp.IView
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

interface RankContract {
    interface Model {
        fun fetchRank(type: String, page: Int): Observable<Response<RankBean?>>
    }

    interface Presenter {
        fun fetchRank(type: String, page: Int)
    }

    interface View : IView {
        fun bindRankData(data: RankBean?)
    }
}