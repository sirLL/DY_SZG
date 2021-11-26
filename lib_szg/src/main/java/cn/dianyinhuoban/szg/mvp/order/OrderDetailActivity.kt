package cn.dianyinhuoban.szg.mvp.order

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import cn.dianyinhuoban.szg.R
import cn.dianyinhuoban.szg.mvp.PreviewImageActivity
import cn.dianyinhuoban.szg.mvp.bean.OrderBean
import cn.dianyinhuoban.szg.mvp.order.contract.OrderDetailContract
import cn.dianyinhuoban.szg.mvp.order.presenter.OrderDetailPresenter
import cn.dianyinhuoban.szg.widget.dialog.MessageDialog
import coil.load
import coil.transform.RoundedCornersTransformation
import com.tencent.mmkv.MMKV
import com.wareroom.lib_base.ui.BaseActivity
import com.wareroom.lib_base.utils.DateTimeUtils
import com.wareroom.lib_base.utils.DimensionUtils
import com.wareroom.lib_base.utils.NumberUtils
import com.wareroom.lib_base.utils.OSUtils
import kotlinx.android.synthetic.main.dy_activity_order_detail.*

class OrderDetailActivity : BaseActivity<OrderDetailPresenter?>(), OrderDetailContract.View {
    private var mOrderID: String? = null
    private val dp2px3: Float by lazy {
        DimensionUtils.dp2px(this, 3).toFloat()
    }

    companion object {
        fun open(context: Context, orderID: String?) {
            val intent = Intent(context, OrderDetailActivity::class.java)
            val bundle = Bundle()
            bundle.putString("orderID", orderID)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
    }

    override fun handleIntent(bundle: Bundle?) {
        super.handleIntent(bundle)
        mOrderID = bundle?.getString("orderID", "")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dy_activity_order_detail)
        setTitle("采购详情")
        fetchOrder()

        btn_submit.setOnClickListener {
            confirmReceipt()
        }
        tv_call.setOnClickListener {
            val phone = MMKV.defaultMMKV().decodeString("COMPANY_PHONE", "")
            OSUtils.callPhone(this, phone)
        }
    }

    override fun getPresenter(): OrderDetailPresenter? {
        return OrderDetailPresenter(this)
    }

    private fun fetchOrder() {
        mPresenter?.fetchPurchaseRecord(mOrderID ?: "")
    }

    override fun bindOrder(order: OrderBean?) {
        iv_status.setImageResource(getStatusIcon(order?.status ?: ""))
        tv_status.text = getOrderStatusName(order?.status ?: "")
        tv_status_des.text = getOrderStatusDes(order?.status ?: "")
        tv_name_phone.text = "${order?.contact ?: ""}  ${order?.telephone ?: ""}"
        tv_order_area.text = order?.area ?: ""
        tv_order_detail_address.text = order?.address ?: ""
        tv_order_time.text =
            DateTimeUtils.getYYYYMMDDHHMMSS((order?.inputTime?.toLong() ?: 0) * 1000)

        ll_logistics_container.visibility = if ("3" == order?.status || "4" == order?.status) {
            View.VISIBLE
        } else {
            View.GONE
        }
        tv_logistics_company.text = order?.expressBrand ?: ""
        tv_logistics_no.text = order?.expressNo ?: ""

        img_order_thumb?.load(order?.img ?: "") {
            crossfade(true)
            placeholder(R.drawable.dy_ic_app_logo)
            error(R.drawable.dy_ic_app_logo)
        }
        tv_order_no.text = order?.purchaseNo
        tv_order_status.text = getOrderStatusName(order?.status ?: "")
        tv_order_product.text = order?.machineName ?: ""
        tv_order_price.text = "¥${NumberUtils.formatMoney(order?.price)}"
        tv_order_count.text = "x${order?.num}"
        val num = NumberUtils.string2BigDecimal(order?.num)
        val price = NumberUtils.string2BigDecimal(order?.price)
        val amount = num.multiply(price)
        tv_order_amount.text == "应付：${NumberUtils.formatMoney(amount)}"

        tv_sn.text = "No: ${order?.purchaseNo ?: "--"}"
        tv_pay_type.text = getPayTypeName(order?.payType ?: "")

        fl_btn_container.visibility = if ("3" == order?.status) {
            View.VISIBLE
        } else {
            View.GONE
        }

        fl_voucher.visibility = if ("6" == order?.payType) {
            View.VISIBLE
        } else {
            View.GONE
        }
        iv_voucher?.load(order?.payment_voucher) {
            crossfade(true)
        }
        iv_voucher?.setOnClickListener {
            if (!order?.payment_voucher.isNullOrBlank()) {
                PreviewImageActivity.open(
                    this,
                    arrayOf(order?.payment_voucher!!)
                )
            }
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

    private fun getOrderStatusDes(status: String): String {
        return when (status) {
            "1" -> {
                "您尚未付款，请及时付款"
            }
            "2" -> {
                "请耐心等待，总部正在发货中"
            }
            "3" -> {
                "已发货，请等待到货"
            }
            "4" -> {
                "恭喜您，采购成功"
            }
            "5" -> {
                "支付信息正在审核中，请耐心等待"
            }
            "6" -> {
                "抱歉，支付信息审核未通过"
            }
            "-1" -> {
                "退款成功"
            }
            else -> {
                ""
            }
        }
    }

    private fun getStatusIcon(status: String): Int {
        return when (status) {
            "1" -> {
                R.drawable.dy_ic_order_state_wait_ship
            }
            "2" -> {
                R.drawable.dy_ic_order_state_wait_ship
            }
            "3" -> {
                R.drawable.dy_ic_order_state_sure
            }
            "4" -> {
                R.drawable.dy_ic_order_state_complete
            }
            "5" -> {
                R.drawable.dy_ic_order_state_wait_ship
            }
            "6" -> {
                R.drawable.dy_ic_order_state_fail
            }
            "-1" -> {
                R.drawable.dy_ic_order_state_complete
            }
            else -> {
                R.drawable.dy_ic_order_state_wait_ship
            }
        }
    }

    private fun getPayTypeName(payType: String): String {
        return when (payType) {
            "1" -> {
                "余额"
            }
            "2" -> {
                "积分"
            }
            "3" -> {
                "支付宝APP支付"
            }
            "4" -> {
                "微信"
            }
            "5" -> {
                "支付宝WAP支付"
            }
            "6" -> {
                "现金支付"
            }
            else -> {
                ""
            }
        }
    }

    private fun confirmReceipt() {
        MessageDialog(this)
            .setTitle("确认收货")
            .setMessage("您是否已收到机具?")
            .setOnConfirmClickListener {
                it.dismiss()
                mPresenter?.submitConfirmReceipt(mOrderID ?: "")
            }.show()
    }

    override fun onConfirmReceiptSuccess() {

    }
}