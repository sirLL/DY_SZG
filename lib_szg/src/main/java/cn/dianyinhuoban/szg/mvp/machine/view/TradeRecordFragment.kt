package cn.dianyinhuoban.szg.mvp.machine.view

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import cn.dianyinhuoban.szg.R
import cn.dianyinhuoban.szg.mvp.bean.MachineTradeRecordBean
import cn.dianyinhuoban.szg.mvp.machine.contract.MachineTradeRecordContract
import cn.dianyinhuoban.szg.mvp.machine.presenter.MachineTradeRecordPresenter
import com.wareroom.lib_base.ui.BaseListFragment
import com.wareroom.lib_base.ui.adapter.SimpleAdapter
import com.wareroom.lib_base.utils.DateTimeUtils
import com.wareroom.lib_base.utils.NumberUtils
import kotlinx.android.synthetic.main.dy_item_machine_trade_record.view.*

class TradeRecordFragment :
    BaseListFragment<MachineTradeRecordBean, MachineTradeRecordPresenter?>(),
    MachineTradeRecordContract.View {
    private var mMachineSN: String? = null

    companion object {
        fun newInstance(sn: String?): TradeRecordFragment {
            val args = Bundle()
            args.putString("sn", sn)
            val fragment = TradeRecordFragment()
            fragment.arguments = args
            return fragment
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onRequest(DEF_START_PAGE)
    }

    override fun getPresenter(): MachineTradeRecordPresenter? {
        return MachineTradeRecordPresenter(this)
    }

    override fun onRequest(page: Int) {
        mPresenter?.fetchTradeRecord(mMachineSN ?: "", page)
    }

    override fun getItemLayout(): Int {
        return R.layout.dy_item_machine_trade_record
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mMachineSN = arguments?.getString("sn", "")
    }

    override fun convert(
        viewHolder: SimpleAdapter.SimpleViewHolder?,
        position: Int,
        itemData: MachineTradeRecordBean?
    ) {
        viewHolder?.itemView?.tv_title?.text = itemData?.trade_type ?: "--"
        viewHolder?.itemView?.tv_status?.text = itemData?.pos_sn ?: "--"
        viewHolder?.itemView?.tv_amount?.text = NumberUtils.formatMoney(itemData?.profit)
        viewHolder?.itemView?.tv_date?.text = if (!TextUtils.isEmpty(itemData?.trade_time)) {
            DateTimeUtils.getYYYYMMDDHHMMSS(itemData?.trade_time!!.toLong() * 1000)
        } else {
            "--"
        }
    }

    override fun onItemClick(data: MachineTradeRecordBean?, position: Int) {

    }

    override fun bindTradeRecord(data: List<MachineTradeRecordBean>?) {
        loadData(data)
    }


}