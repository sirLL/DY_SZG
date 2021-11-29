package cn.dianyinhuoban.szg.mvp.income.view

import android.os.Bundle
import android.view.View
import cn.dianyinhuoban.szg.R
import cn.dianyinhuoban.szg.bean.IntegralRecordBean
import cn.dianyinhuoban.szg.mvp.income.contract.IntegralRecordContract
import cn.dianyinhuoban.szg.mvp.income.presenter.IntegralRecordPresenter
import com.wareroom.lib_base.ui.BaseListFragment
import com.wareroom.lib_base.ui.adapter.SimpleAdapter

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
        viewHolder?.setText(R.id.tv_sn, "SN:${itemData?.pos_sn ?: "--"}")
        viewHolder?.setText(R.id.tv_amount, itemData?.value ?: "--")
        viewHolder?.setText(R.id.tv_date, itemData?.input_time ?: "--")
    }

    override fun onItemClick(data: IntegralRecordBean?, position: Int) {

    }
}