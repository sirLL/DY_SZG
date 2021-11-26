package cn.dianyinhuoban.szg.mvp.order

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import cn.dianyinhuoban.szg.R
import cn.dianyinhuoban.szg.mvp.bean.OrderBean
import cn.dianyinhuoban.szg.mvp.order.contract.OrderListContract
import cn.dianyinhuoban.szg.mvp.order.presenter.OrderListPresenter
import coil.load
import coil.transform.RoundedCornersTransformation
import com.wareroom.lib_base.ui.BaseListFragment
import com.wareroom.lib_base.ui.adapter.SimpleAdapter
import com.wareroom.lib_base.utils.DateTimeUtils
import com.wareroom.lib_base.utils.DimensionUtils
import com.wareroom.lib_base.utils.NumberUtils
import kotlinx.android.synthetic.main.dy_item_pos_order.view.*

class OrderListFragment : BaseListFragment<OrderBean, OrderListPresenter?>(),
    OrderListContract.View {
    private var mStatus: String? = null
    private val dp2px3: Float by lazy {
        DimensionUtils.dp2px(requireContext(), 3).toFloat()
    }

    companion object {
        fun newInstance(status: String): OrderListFragment {
            val args = Bundle()
            args.putString("status", status)
            val fragment = OrderListFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mStatus = arguments?.getString("status")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onRequest(DEF_START_PAGE)
    }

    override fun getPresenter(): OrderListPresenter? {
        return OrderListPresenter(this)
    }

    override fun onRequest(page: Int) {
        mPresenter?.fetchPurchaseRecordList(mStatus ?: "", page)
    }

    override fun initView(contentView: View?) {
        super.initView(contentView)
        showLoadingView()
        onRequest(DEF_START_PAGE)
    }

    override fun getItemLayout(): Int {
        return R.layout.dy_item_pos_order
    }

    override fun bindPurchaseRecord(data: List<OrderBean>?) {
        loadData(data)
    }

    override fun convert(
        viewHolder: SimpleAdapter.SimpleViewHolder?,
        position: Int,
        itemData: OrderBean?,
    ) {
        viewHolder?.itemView?.tv_order_no?.text = "No: ${itemData?.purchaseNo ?: "--"}"
        viewHolder?.itemView?.tv_order_status?.text = getOrderStatusName(itemData?.status ?: "")
        viewHolder?.itemView?.tv_order_status?.setTextColor(getStatusColor(itemData?.status ?: ""))
        viewHolder?.itemView?.tv_order_product?.text = itemData?.machineName ?: "--"
        viewHolder?.itemView?.tv_order_price?.text = "¥${NumberUtils.formatMoney(itemData?.price)}"
        viewHolder?.itemView?.tv_order_count?.text = "x${itemData?.num}"
        //支付方式 1余额，2积分，3支付宝APP支付，4微信 5支付宝WAP支付 6 现金支付
        viewHolder?.itemView?.tv_pay_type?.text = when (itemData?.payType) {
            "1" -> {
                "支付方式:余额"
            }
            "2" -> {
                "支付方式:积分"
            }
            "3" -> {
                "支付方式:支付宝APP支付"
            }
            "4" -> {
                "支付方式:微信"
            }
            "5" -> {
                "支付方式:支付宝WAP支付"
            }
            "6" -> {
                "支付方式:现金支付"
            }
            else -> {
                ""
            }
        }
        viewHolder?.itemView?.tv_date?.text = if (itemData?.inputTime.isNullOrBlank()) {
            ""
        } else {
            DateTimeUtils.formatDate(
                itemData?.inputTime?.toLong()!! * 1000,
                DateTimeUtils.PATTERN_YYYY_MM_DD_HH_MM_SS
            )
        }
        val num = NumberUtils.string2BigDecimal(itemData?.num)
        val price = NumberUtils.string2BigDecimal(itemData?.price)
        val amount = num.multiply(price)
        viewHolder?.itemView?.tv_order_amount?.text = "订单总额：${NumberUtils.formatMoney(amount)}"


        viewHolder?.itemView?.img_order_thumb?.load(itemData?.img ?: "") {
            crossfade(true)
            placeholder(R.drawable.dy_ic_app_logo)
            error(R.drawable.dy_ic_app_logo)
            transformations(RoundedCornersTransformation(dp2px3, dp2px3, dp2px3, dp2px3))
        }
    }

    override fun onItemClick(data: OrderBean?, position: Int) {
        data.let {
            OrderDetailActivity.open(requireContext(), data?.id)
        }

    }

    private fun getOrderStatusName(status: String): String {
        return when (status) {
            "1" -> {
                "未支付"
            }
            "2" -> {
                "待发货"
            }
            "3" -> {
                "待确认"
            }
            "4" -> {
                "已完成"
            }
            "5" -> {
                "支付审核中"
            }
            "6" -> {
                "支付审核未通过"
            }
            "-1" -> {
                "已退款"
            }
            else -> {
                "--"
            }
        }
    }

    private fun getStatusColor(status: String): Int {
        return when (status) {
            "1" -> {
                ContextCompat.getColor(requireContext(), R.color.color_f60e36)
            }
            "2" -> {
                ContextCompat.getColor(requireContext(), R.color.color_f60e36)
            }
            "3" -> {
                ContextCompat.getColor(requireContext(), R.color.color_999999)
            }
            "4" -> {
                ContextCompat.getColor(requireContext(), R.color.color_999999)
            }
            "5" -> {
                ContextCompat.getColor(requireContext(), R.color.color_999999)
            }
            "6" -> {
                ContextCompat.getColor(requireContext(), R.color.color_f60e36)
            }
            "-1" -> {
                ContextCompat.getColor(requireContext(), R.color.color_f60e36)
            }
            else -> {
                ContextCompat.getColor(requireContext(), R.color.color_999999)
            }
        }
    }

}