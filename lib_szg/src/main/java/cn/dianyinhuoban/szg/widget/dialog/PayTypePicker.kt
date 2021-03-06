package cn.dianyinhuoban.szg.widget.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.dianyinhuoban.szg.R
import cn.dianyinhuoban.szg.mvp.bean.*
import cn.dianyinhuoban.szg.mvp.me.contract.MeContract
import cn.dianyinhuoban.szg.mvp.me.presenter.MePresenter
import cn.dianyinhuoban.szg.mvp.order.contract.PayTypeContract
import cn.dianyinhuoban.szg.mvp.order.presenter.PayTypePresenter
import coil.load
import com.wareroom.lib_base.ui.adapter.SimpleAdapter
import com.wareroom.lib_base.utils.NumberUtils
import kotlinx.android.synthetic.main.dy_item_pay_type_picker.view.*

class PayTypePicker : BaseBottomPicker<PayTypeBean?, PayTypeContract.Presenter>(),
    PayTypeContract.View {
    companion object {
        fun newInstance(machineType: String): PayTypePicker {
            val args = Bundle()
            args.putString("machineType", machineType)
            val fragment = PayTypePicker()
            fragment.arguments = args
            return fragment
        }
    }

    private var mCheckedPosition = -1
    private var machineType: String = ""
    private var mOfflinePayInfo: OfflinePayInfoBean? = null
    private var mIntegralData: List<IntegralBalanceBean>? = null

    override fun getTitle(): String {
        return "选择支付方式"
    }

    override fun isSupportLoadMore(): Boolean {
        return false
    }

    override fun getItemLayoutRes(): Int {
        return R.layout.dy_item_pay_type_picker
    }

    override fun getPresenter(): PayTypeContract.Presenter {
        return PayTypePresenter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        machineType = arguments?.getString("machineType", "") ?: ""
        return super.onCreateView(inflater, container, savedInstanceState)
    }


    override fun onStart() {
        super.onStart()
        request(DEFAULT_BUFFER_SIZE)
    }

    override fun convert(
        viewHolder: SimpleAdapter.SimpleViewHolder?,
        position: Int,
        itemData: PayTypeBean?,
    ) {
        if (itemData != null) {
            viewHolder?.itemView?.iv_cover?.load(itemData.iconRes)
            viewHolder?.itemView?.tv_title?.text = itemData.name
            viewHolder?.itemView?.tv_balance?.text = when (itemData.id) {
                1L -> {
                    "当前余额：¥${NumberUtils.formatMoney(itemData.balance)}"
                }
                2L -> {
                    "当前积分：¥${itemData.balance}"
                }
                else -> {
                    ""
                }
            }

            viewHolder?.itemView?.tv_balance?.visibility =
                if (1L == itemData.id || 2L == itemData.id) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            viewHolder?.itemView?.iv_hook?.visibility = if (position == mCheckedPosition) {
                View.VISIBLE
            } else {
                View.GONE
            }
        } else {
            viewHolder?.itemView?.iv_cover?.load(0)
            viewHolder?.itemView?.tv_title?.text = ""
            viewHolder?.itemView?.tv_balance?.text = ""
            viewHolder?.itemView?.iv_hook?.visibility = View.GONE
        }
    }

    override fun onItemClick(d: PayTypeBean?, position: Int) {
        d?.let {
            mCheckedPosition = position
            getAdapter()?.notifyDataSetChanged()
            super.onItemClick(d, position)
        }
    }

    override fun request(page: Int) {
        mPresenter?.fetchIntegralBalance(machineType)
        mPresenter?.fetchOfflinePayInfo()
    }


    override fun bindIntegralBalance(data: List<IntegralBalanceBean>?) {
        mIntegralData = data
        buildData()

    }

    override fun bindOfflinePayInfo(payInfoBean: OfflinePayInfoBean?) {
        mOfflinePayInfo = payInfoBean
        buildData()
    }

    private fun buildData() {
        val payTypeData = mutableListOf<PayTypeBean?>()
//        payTypeData.add(
//            PayTypeBean(
//                1,
//                R.drawable.dy_ic_pay_type_balance,
//                "余额支付",
//                personalBean?.total ?: "0"
//            )
//        )
        var integralBalanceBean: IntegralBalanceBean? = null
        mIntegralData?.let {
            for (bean in it) {
                if (machineType == bean.machineTypeId) {
                    integralBalanceBean = bean
                }
            }
        }
        payTypeData.add(
            PayTypeBean(
                2,
                R.drawable.dy_ic_pay_type_integral,
                "积分支付",
                integralBalanceBean?.point ?: "0"
            )
        )

//        payTypeData.add(
//            PayTypeBean(
//                5,
//                R.drawable.dy_ic_pay_type_alipay,
//                "支付宝支付",
//                "0"
//            )
//        )
        mOfflinePayInfo?.let {
            if ("1" == it.payType || "3" == it.payType) {
                payTypeData.add(
                    PayTypeBean(
                        6,
                        R.drawable.dy_ic_pay_type_offline,
                        "现金支付",
                        "0"
                    )
                )
            }

            if ("2" == it.payType || "3" == it.payType) {
                payTypeData.add(
                    PayTypeBean(
                        7,
                        R.drawable.dy_ic_pay_type_scan,
                        "扫码支付",
                        "0"
                    )
                )
            }
        }

        loadData(payTypeData)
    }

}