package cn.dianyinhuoban.szg.mvp.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class PurchaseProductBean(
    var describe: String?,
    var id: String?,
    var img: String?,
    var name: String?,
    var price: String?,
    var set_meal: String?,
    var typeName: String?,
    var type_id: String?,
    var act_cashback:String?,
    var standard_cashback:String?,
    var back_point:String?
) : Parcelable