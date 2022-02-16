package cn.dianyinhuoban.szg.mvp.income.view

import android.os.Bundle
import android.view.View
import android.widget.TextView
import cn.dianyinhuoban.szg.R
import cn.dianyinhuoban.szg.bean.IntegralRecordBean
import cn.dianyinhuoban.szg.mvp.income.contract.IntegralRecordContract
import cn.dianyinhuoban.szg.mvp.income.presenter.IntegralRecordPresenter
import cn.dianyinhuoban.szg.widget.dialog.MessageDialog
import com.wareroom.lib_base.ui.BaseListFragment
import com.wareroom.lib_base.ui.adapter.SimpleAdapter
import com.wareroom.lib_base.utils.NumberUtils

class IntegralRecordFragment :
    BaseListFragment<IntegralRecordBean?, IntegralRecordContract.Presenter?>(),
    IntegralRecordContract.View {
    companion object {
        fun newInstance(machineType: String): IntegralRecordFragment {
            val args = Bundle()
            args.putString("machineType", machineType)
            val fragment = IntegralRecordFragment()
            fragment.arguments = args
            return fragment
        }
    }

    var machineType = ""
    override fun initView(contentView: View?) {
        machineType = arguments?.getString("machineType", "") ?: ""
        super.initView(contentView)
        mRefreshLayout?.autoRefresh()
    }

    override fun getPresenter(): IntegralRecordContract.Presenter? {
        return IntegralRecordPresenter(this)
    }

    override fun onRequest(page: Int) {
        mPresenter?.fetchIntegralRecord(machineType, "", page)
    }

    override fun getItemLayout(): Int {
        return R.layout.dy_item_integral_record
    }

    override fun bindIntegralRecord(data: List<IntegralRecordBean>?) {
        loadData(data)
    }

    override fun convert(
        viewHolder: SimpleAdapter.SimpleViewHolder?,
        position: Int,
        itemData: IntegralRecordBean?
    ) {
        var exchangeButton = viewHolder?.getView<TextView>(R.id.tv_exchange)
        viewHolder?.setText(R.id.tv_sn, "SN:${itemData?.pos_sn ?: "--"}")
        viewHolder?.setText(R.id.tv_amount, NumberUtils.formatMoney(itemData?.value) ?: "--")
        viewHolder?.setText(R.id.tv_date, itemData?.input_time ?: "--")
        exchangeButton?.visibility = if ("1" == machineType) {
            View.VISIBLE
        } else {
            View.GONE
        }
        exchangeButton?.setOnClickListener {
            showExchangeDialog(itemData)
        }
    }

    override fun onItemClick(data: IntegralRecordBean?, position: Int) {
        if (!data?.id.isNullOrEmpty()) {
            IntegralDetailActivity.open(requireContext(), data?.id!!)
        }
    }

    private fun showExchangeDialog(record: IntegralRecordBean?) {
        if (record != null && !record.id.isNullOrEmpty()) {
            val dialog = MessageDialog(requireContext())
            dialog.setMessage("是否变现${NumberUtils.formatMoney(record.value) ?: "--"}积分？")
            dialog.setOnConfirmClickListener {
                it.dismiss()
                submitExchange(record.id)
            }
            dialog.show()
        }
    }

    private fun submitExchange(recordID: String?) {
        recordID?.let {
            mPresenter?.submitIntegral2Balance(it)
        }
    }

    override fun onIntegral2BalanceSuccess() {
        showToast("变现成功")
        mRefreshLayout?.autoRefresh()
    }
}