package cn.dianyinhuoban.szg.mvp.machine.view

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import cn.dianyinhuoban.szg.R
import cn.dianyinhuoban.szg.mvp.bean.*
import cn.dianyinhuoban.szg.mvp.machine.contract.MachineManagerNewContract
import cn.dianyinhuoban.szg.mvp.machine.presenter.MachineManagerNewPresenter
import cn.dianyinhuoban.szg.widget.dialog.MachineFilterDialog
import coil.load
import com.github.gzuliyujiang.wheelpicker.DatePicker
import com.github.gzuliyujiang.wheelpicker.annotation.DateMode
import com.github.gzuliyujiang.wheelpicker.entity.DateEntity
import com.wareroom.lib_base.ui.BaseListFragment
import com.wareroom.lib_base.ui.adapter.SimpleAdapter
import com.wareroom.lib_base.utils.DateTimeUtils
import com.wareroom.lib_base.utils.NumberUtils
import java.util.*

class MachineManagerChildFragment :
    BaseListFragment<MachineItemData, MachineManagerNewContract.Presenter?>(),
    MachineManagerNewContract.View {
    companion object {
        fun newInstance(typeID: String): MachineManagerChildFragment {
            val args = Bundle()
            args.putString("typeID", typeID)
            val fragment = MachineManagerChildFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private var machineTypeID = ""
    private var machineStatus = ""
    private var startDate = ""
    private var endDate = ""
    private var machineSN = ""
    private var tvTabUnbind: TextView? = null
    private var tvTabInactivated: TextView? = null
    private var tvTabActivated: TextView? = null
    private var tvPersonTotal: TextView? = null
    private var tvTeamTotal: TextView? = null
    private var datePicker: DatePicker? = null
    private var filterDialog: MachineFilterDialog? = null

    override fun getContentView(): Int {
        return R.layout.dy_fragment_machine_manager_child
    }

    override fun getItemLayout(): Int {
        return R.layout.dy_item_machine_manager_child
    }

    override fun getPresenter(): MachineManagerNewContract.Presenter? {
        return MachineManagerNewPresenter(this)
    }

    override fun initView(contentView: View?) {
        super.initView(contentView)
        machineTypeID = arguments?.getString("typeID", "") ?: ""
        tvTabUnbind = contentView?.findViewById(R.id.tv_tab_unbind)
        tvTabInactivated = contentView?.findViewById(R.id.tv_tab_inactivated)
        tvTabActivated = contentView?.findViewById(R.id.tv_tab_activated)
        tvPersonTotal = contentView?.findViewById(R.id.tv_person_total)
        tvTeamTotal = contentView?.findViewById(R.id.tv_team_total)

        tvTabUnbind?.setOnClickListener {
            changeMachineStatus("1")
        }
        tvTabInactivated?.setOnClickListener {
            changeMachineStatus("2")
        }
        tvTabActivated?.setOnClickListener {
            changeMachineStatus("3")
        }
        changeMachineStatus("1")
    }

    private fun changeMachineStatus(status: String) {
        machineStatus = status
        tvTabActivated?.isSelected = "3" == machineStatus
        tvTabInactivated?.isSelected = "2" == machineStatus
        tvTabUnbind?.isSelected = "1" == machineStatus
        mRefreshLayout?.autoRefresh()
    }

    fun showFilterDialog() {
        if (filterDialog == null) {
            filterDialog = MachineFilterDialog.newInstance()
        }
        filterDialog?.setInputSN(machineSN)?.setStartDate(startDate)?.setEndDate(endDate)
            ?.setOnMachineFilterViewClick(object :
                MachineFilterDialog.OnMachineFilterViewClick {
                override fun onSearchClick(sn: String, startDate: String, endDate: String) {
                    this@MachineManagerChildFragment.machineSN = sn
                    this@MachineManagerChildFragment.startDate = startDate
                    this@MachineManagerChildFragment.endDate = endDate
                    this@MachineManagerChildFragment.mRefreshLayout?.autoRefresh()
                }

                override fun onStartDateClick(filterDialog: MachineFilterDialog) {
                    filterDialog.dismiss()
                    showDatePicker(1)
                }

                override fun onEndDateClick(filterDialog: MachineFilterDialog) {
                    filterDialog.dismiss()
                    showDatePicker(2)
                }
            })?.show(childFragmentManager, "MachineFilterDialog")
    }

    override fun onRequest(page: Int) {
        mPresenter?.fetchMachine(machineTypeID, machineStatus, machineSN, startDate, endDate, page)
    }

    override fun bindMachine(data: MyMachineBeanNew?) {
        tvTabUnbind?.text = "未绑定(台)${
            if (data?.machineData?.unBind.isNullOrBlank()) {
                "--"
            } else {
                data?.machineData?.unBind
            }
        }"
        tvTabActivated?.text = "已激活(台)${
            if (data?.machineData?.active.isNullOrBlank()) {
                "--"
            } else {
                data?.machineData?.active
            }
        }"
        tvTabInactivated?.text = "未激活(台)${
            if (data?.machineData?.nonActive.isNullOrBlank()) {
                "--"
            } else {
                data?.machineData?.nonActive
            }
        }"
        tvPersonTotal?.text = data?.machineData?.total ?: "--"
        tvTeamTotal?.text = data?.machineData?.teamMachineTotal ?: "--"
        loadData(data?.data)
    }

    override fun convert(
        viewHolder: SimpleAdapter.SimpleViewHolder?,
        position: Int,
        itemData: MachineItemData?
    ) {
        viewHolder?.getView<ImageView>(R.id.iv_cover)?.load(itemData?.img) {
            crossfade(true)
        }
        viewHolder?.setText(R.id.tv_sn, "SN:${itemData?.pos_sn ?: "--"}")
        viewHolder?.setText(
            R.id.tv_back_money, "返现金额:${
                if (itemData?.backMoney.isNullOrBlank()) {
                    "--"
                } else {
                    NumberUtils.numberScale(itemData!!.backMoney)
                }
            }"
        )

        val tvEndDate = viewHolder?.getView<TextView>(R.id.tv_end_date)
        tvEndDate?.visibility = if (itemData?.back_time.isNullOrBlank()) {
            View.GONE
        } else {
            View.VISIBLE
        }
        tvEndDate?.text = "截至返现时间:${
            if (itemData?.back_time.isNullOrBlank()) {
                ""
            } else {
                itemData?.back_time
            }
        }"


        when (machineStatus) {
            "1" -> {
                //未绑定
                viewHolder?.setText(
                    R.id.tv_date, "入库时间 ${itemData?.add_time ?: "--"}"
                )
            }
            "2" -> {
                //未激活
                viewHolder?.setText(
                    R.id.tv_date, "绑定时间 ${itemData?.transfer_time ?: "--"}"
                )
            }
            "3" -> {
                //已激活
                viewHolder?.setText(
                    R.id.tv_date, "激活时间 ${itemData?.act_time ?: "--"}"
                )
            }
            else -> {
                viewHolder?.setText(
                    R.id.tv_date, ""
                )
            }
        }

    }

    override fun onItemClick(data: MachineItemData?, position: Int) {
        data?.let {
            MachineDetailActivity.openMachineDetailActivity(requireContext(), it, machineStatus)
        }
    }

    private fun showDatePicker(actionCode: Int) {
        var nowCalendar = Calendar.getInstance()
        datePicker = DatePicker(requireActivity(), R.style.BottomDialog)
        val wheelLayout = datePicker?.wheelLayout
        wheelLayout?.setDateMode(DateMode.YEAR_MONTH_DAY)
        wheelLayout?.setDateLabel("年", "月", "日")
        wheelLayout?.setRange(
            DateEntity.target(nowCalendar.get(Calendar.YEAR) - 20, 1, 1),
            DateEntity.target(nowCalendar.get(Calendar.YEAR) + 20, 12, 31),
            DateEntity.today()
        )

        datePicker?.titleView?.text = "选择开始日期"
        datePicker?.titleView?.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.color_333333
            )
        )
        datePicker?.titleView?.textSize = 18f

        datePicker?.cancelView?.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.color_999999
            )
        )
        datePicker?.cancelView?.textSize = 14f

        datePicker?.okView?.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.colorPrimary
            )
        )
        datePicker?.okView?.textSize = 14f

        datePicker?.contentView?.setBackgroundColor(Color.TRANSPARENT)
        datePicker?.bodyView?.setBackgroundColor(Color.WHITE)
        datePicker?.topLineView?.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.dy_color_divider
            )
        )
        datePicker?.headerView?.setBackgroundResource(R.drawable.dy_shape_ffffff_radius_top_6)
        datePicker?.setOnDatePickedListener { year, month, day ->
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month - 1)
            calendar.set(Calendar.DAY_OF_MONTH, day)

            if (actionCode == 1) {
                startDate = DateTimeUtils.formatDate(
                    calendar.timeInMillis,
                    DateTimeUtils.PATTERN_YYYY_MM_DD
                )
            } else {
                endDate = DateTimeUtils.formatDate(
                    calendar.timeInMillis,
                    DateTimeUtils.PATTERN_YYYY_MM_DD
                )
            }
            showFilterDialog()
        }

        if (!datePicker?.isShowing!!) {
            datePicker?.show()
        }
    }
}