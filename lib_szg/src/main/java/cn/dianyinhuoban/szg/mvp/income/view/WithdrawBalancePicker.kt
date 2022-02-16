package cn.dianyinhuoban.szg.mvp.income.view

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import cn.dianyinhuoban.szg.R
import cn.dianyinhuoban.szg.bean.BalanceTypeBean
import cn.dianyinhuoban.szg.mvp.bean.BankBean
import cn.dianyinhuoban.szg.mvp.bean.PersonalBean
import cn.dianyinhuoban.szg.mvp.income.contract.BankCardListContract
import cn.dianyinhuoban.szg.mvp.income.contract.WithdrawContract
import cn.dianyinhuoban.szg.mvp.income.presenter.BankCardListPresenter
import cn.dianyinhuoban.szg.mvp.income.presenter.WithdrawPresenter
import cn.dianyinhuoban.szg.mvp.me.contract.MeContract
import cn.dianyinhuoban.szg.mvp.me.view.BindBankCardActivity
import cn.dianyinhuoban.szg.util.StringUtil
import cn.dianyinhuoban.szg.widget.dialog.BaseBottomPicker
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.ui.adapter.SimpleAdapter
import kotlinx.android.synthetic.main.dy_base_bottom_picker.*
import kotlinx.android.synthetic.main.dy_fragment_withdraw_type_picker.*
import kotlinx.android.synthetic.main.dy_fragment_withdraw_type_picker.recycler_view
import kotlinx.android.synthetic.main.dy_item_withdraw_type.view.*

class WithdrawBalancePicker : BaseBottomPicker<BalanceTypeBean, IPresenter?>() {
    companion object {
        fun newInstance(): WithdrawBalancePicker {
            val args = Bundle()
            val fragment = WithdrawBalancePicker()
            fragment.arguments = args
            return fragment
        }
    }

    private var mCheckedID: String? = null

    override fun isSupportLoadMore(): Boolean {
        return false
    }

    override fun getTitle(): String {
        return "提现余额"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        request(DEF_START_PAGE)
    }

    fun setCheckedID(checkedID: String?) {
        if (TextUtils.isEmpty(mCheckedID)) {
            mCheckedID = checkedID
        }
        mAdapter?.let {
            it.notifyDataSetChanged()
        }
    }

    override fun setupRecyclerView() {
        mAdapter = object : SimpleAdapter<BalanceTypeBean>(getItemLayoutRes()) {
            override fun convert(
                viewHolder: SimpleViewHolder?,
                position: Int,
                itemData: BalanceTypeBean?
            ) {
                this@WithdrawBalancePicker.convert(viewHolder, position, itemData)
            }

            override fun onItemClick(data: BalanceTypeBean?, position: Int) {
                if (mData == null || mData.size == position) {
                    //添加银行卡
                    startActivity(Intent(context, BindBankCardActivity::class.java))
                    dismiss()
                } else {
                    mCheckedID = data?.id
                    mAdapter?.notifyItemChanged(position)
                    this@WithdrawBalancePicker.onItemClick(data, position)
                    dismiss()
                }
            }

        }
        recycler_view.adapter = mAdapter
    }

    override fun getItemLayoutRes(): Int {
        return R.layout.dy_item_withdraw_balance
    }

    override fun getPresenter(): WithdrawPresenter? {
        return null
    }

    override fun convert(
        viewHolder: SimpleAdapter.SimpleViewHolder?,
        position: Int,
        itemData: BalanceTypeBean?
    ) {
        viewHolder?.itemView?.iv_hook?.visibility =
            if (!TextUtils.isEmpty(mCheckedID) && mCheckedID == itemData?.id) {
                View.VISIBLE
            } else {
                View.INVISIBLE
            }
        viewHolder?.itemView?.iv_icon?.setImageResource(R.drawable.dy_ic_bank_card_picker)
        viewHolder?.itemView?.tv_title?.text = itemData?.name ?: "--"
    }

    override fun request(page: Int) {
        val data = mutableListOf<BalanceTypeBean>()
        data.add(BalanceTypeBean("3", "个人收益"))
        data.add(BalanceTypeBean("2", "激活返现"))
        data.add(BalanceTypeBean("1", "其他收益"))
        loadData(data)
    }


}