package cn.dianyinhuoban.szg.mvp.income.view

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import cn.dianyinhuoban.szg.R
import cn.dianyinhuoban.szg.mvp.bean.WithdrawRecordBean
import cn.dianyinhuoban.szg.mvp.bean.WithdrawRecordStatusBean
import cn.dianyinhuoban.szg.mvp.bean.WithdrawRecordTypeBean
import cn.dianyinhuoban.szg.mvp.income.contract.WithdrawRecordContract
import cn.dianyinhuoban.szg.mvp.income.presenter.WithdrawRecordPresenter
import cn.dianyinhuoban.szg.util.StringUtil
import cn.dianyinhuoban.szg.widget.dialog.BaseBottomPicker
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.ui.BaseListFragment
import com.wareroom.lib_base.ui.adapter.SimpleAdapter
import com.wareroom.lib_base.utils.DateTimeUtils
import com.wareroom.lib_base.utils.NumberUtils
import kotlinx.android.synthetic.main.dy_item_withdraw_record.view.*

class WithdrawRecordFragment : BaseListFragment<WithdrawRecordBean, WithdrawRecordPresenter?>(),
    WithdrawRecordContract.View {
    companion object {
        fun newInstance(): WithdrawRecordFragment {
            val args = Bundle()
            val fragment = WithdrawRecordFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private var tvDate: TextView? = null
    private var tvType: TextView? = null
    private var tvStatus: TextView? = null
    private var startTime: String = ""
    private var endTime: String = ""
    private var type: String = ""
    private var status: String = ""

    override fun getContentView(): Int {
        return R.layout.dy_fragment_withdraw_record
    }

    override fun getItemLayout(): Int {
        return R.layout.dy_item_withdraw_record
    }

    override fun getPresenter(): WithdrawRecordPresenter? {
        return WithdrawRecordPresenter(this)
    }

    override fun onRequest(page: Int) {
        mPresenter?.fetchWithdrawRecord(startTime, endTime, type, status, page)
    }

    override fun bindWithdrawRecord(recordData: List<WithdrawRecordBean>?) {
        loadData(recordData)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoadingView()
        onRequest(DEF_START_PAGE)
    }

    override fun initView(contentView: View?) {
        super.initView(contentView)
        tvDate = contentView?.findViewById(R.id.tv_date)
        tvType = contentView?.findViewById(R.id.tv_type)
        tvStatus = contentView?.findViewById(R.id.tv_status)
        contentView?.findViewById<FrameLayout>(R.id.ll_date)?.setOnClickListener {
            var picker: WithdrawRecordDatePicker? =
                childFragmentManager.findFragmentByTag("WithdrawRecordDatePicker") as WithdrawRecordDatePicker?
            if (picker == null) {
                picker = WithdrawRecordDatePicker.newInstance(startTime, endTime)
                picker.setOnDateSelectedCallback(object :
                    WithdrawRecordDatePicker.OnDateSelectedCallback {
                    override fun onDateSelected(startTime: String, endTime: String) {
                        this@WithdrawRecordFragment.onDateSelected(startTime, endTime)
                    }
                })
            }
            picker.show(childFragmentManager, "WithdrawRecordDatePicker")
        }
        contentView?.findViewById<FrameLayout>(R.id.ll_type)?.setOnClickListener {
            val typePicker = WithdrawRecordTypePicker.newInstance()
            typePicker.setOnItemSelectedListener(object :
                BaseBottomPicker.OnItemSelectedListener<WithdrawRecordTypeBean, IPresenter> {
                override fun onItemSelected(
                    bottomPicker: BaseBottomPicker<WithdrawRecordTypeBean, IPresenter>,
                    t: WithdrawRecordTypeBean?,
                    position: Int
                ) {
                    bottomPicker.dismiss()
                    onTypeSelected(t)
                }

            })
            typePicker.show(childFragmentManager, "WithdrawRecordTypePicker")
        }
        contentView?.findViewById<FrameLayout>(R.id.ll_status)?.setOnClickListener {
            val statusPicker = WithdrawRecordStatusPicker.newInstance()
            statusPicker.setOnItemSelectedListener(object :
                BaseBottomPicker.OnItemSelectedListener<WithdrawRecordStatusBean, IPresenter> {
                override fun onItemSelected(
                    bottomPicker: BaseBottomPicker<WithdrawRecordStatusBean, IPresenter>,
                    t: WithdrawRecordStatusBean?,
                    position: Int
                ) {
                    bottomPicker.dismiss()
                    onStatusSelected(t)
                }

            })
            statusPicker.show(childFragmentManager, "WithdrawRecordStatusPicker")
        }
    }

    private fun onDateSelected(startTime: String, endTime: String) {
        this.startTime = startTime
        this.endTime = endTime
        var time = StringBuilder()
        if (startTime.isNotBlank()) {
            time.append(startTime)
        }
        if (endTime.isNotBlank()) {
            if (time.isNotEmpty()) {
                time.append("至")
            }
            time.append(endTime)
        }
        tvDate?.text = if (time.isEmpty()) {
            "全部"
        } else {
            time.toString()
        }
        mRefreshLayout?.autoRefresh()
    }

    private fun onTypeSelected(typeBean: WithdrawRecordTypeBean?) {
        typeBean?.let {
            type = it.id
            tvType?.text = it.title
            mRefreshLayout?.autoRefresh()
        }
    }

    private fun onStatusSelected(statusBean: WithdrawRecordStatusBean?) {
        statusBean?.let {
            status = it.id
            tvStatus?.text = it.title
            mRefreshLayout?.autoRefresh()
        }
    }

    override fun convert(
        viewHolder: SimpleAdapter.SimpleViewHolder?,
        position: Int,
        itemData: WithdrawRecordBean?
    ) {
        var title = StringBuilder()
        if (!itemData?.type.isNullOrBlank()) {
            title.append(itemData?.type)
        }
        if (!itemData?.bankName.isNullOrBlank()) {
            if (title.isNotEmpty()) {
                title.append("-")
            }
            title.append("-${itemData?.bankName}")
            if (!itemData?.bankNo.isNullOrBlank()) {
                var cardNo = if (itemData?.bankNo!!.length > 4) {
                    itemData?.bankNo!!.substring(
                        itemData?.bankNo!!.length - 4,
                        itemData?.bankNo!!.length
                    )
                } else {
                    itemData?.bankNo
                }
                title.append("(${cardNo})")
            }
        }
        viewHolder?.itemView?.tv_title?.text = title.toString()
        viewHolder?.itemView?.tv_amount_?.text = NumberUtils.formatMoney(itemData?.cashAmount)
        viewHolder?.itemView?.tv_amount?.text = NumberUtils.formatMoney(itemData?.amount)
        viewHolder?.itemView?.tv_date?.text = if (itemData?.inputTime.isNullOrBlank()) {
            "--"
        } else {
            DateTimeUtils.getYYYYMMDDHHMMSS(itemData?.inputTime!!.toLong() * 1000)
        }
        viewHolder?.itemView?.tv_date_?.text = if (itemData?.updateTime.isNullOrBlank()) {
            "--"
        } else {
            DateTimeUtils.getYYYYMMDDHHMMSS(itemData?.updateTime!!.toLong() * 1000)
        }
        viewHolder?.itemView?.tv_status?.text = if (itemData?.status.isNullOrBlank()) {
            "--"
        } else {
            getStatusName(itemData?.status!!)
        }
    }

    override fun onItemClick(data: WithdrawRecordBean?, position: Int) {

    }

    //状态 1 处理中 2 成功 3 失败
    private fun getStatusName(status: String): String {
        return if (TextUtils.isEmpty(status)) {
            "--"
        } else {
            when (status) {
                "1" -> {
                    "处理中"
                }
                "2" -> {
                    "成功"
                }
                "3" -> {
                    "失败"
                }
                else -> {
                    "--"
                }
            }
        }
    }


}