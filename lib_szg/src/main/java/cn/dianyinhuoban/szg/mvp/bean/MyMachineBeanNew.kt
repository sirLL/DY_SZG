package cn.dianyinhuoban.szg.mvp.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class MyMachineBeanNew(
    var `data`: List<MachineItemData>?,
    var machineData: MachineData?
) : Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class MachineItemData(
    var act_status: String?,
    var act_time: String?,
    var add_time: String?,
    var back_time: String?,
    var buyType: String?,
    var id: String?,
    var member_id: String?,
    var name: String?,
    var pos_sn: String?,
    var transfer_time: String?,
    var backMoney: String?,
    var img: String
) : Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class MachineData(
    var active: String?,
    var nonActive: String?,
    var teamMachineTotal: String?,
    var total: String?,
    var unBind: String?
) : Parcelable