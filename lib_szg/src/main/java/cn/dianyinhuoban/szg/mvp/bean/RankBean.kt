package cn.dianyinhuoban.szg.mvp.bean

data class RankBean(
    var list: List<RankItemBean>?,
    var myRank: MyRank?
)

data class MyRank(
    var rank: String?,
    var total: String?
)