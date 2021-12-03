package cn.dianyinhuoban.szg.mvp.order.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import cn.dianyinhuoban.szg.R
import cn.dianyinhuoban.szg.event.PaySuccessEvent
import cn.dianyinhuoban.szg.mvp.bean.PurchaseProductBean
import cn.dianyinhuoban.szg.mvp.order.ConfirmOrderActivity
import coil.load
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.ui.BaseActivity
import com.wareroom.lib_base.utils.NumberUtils
import com.wareroom.lib_base.utils.filter.NumberFilter
import kotlinx.android.synthetic.main.dy_activity_product_detail.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


import java.math.BigDecimal

class ProductDetailActivity : BaseActivity<IPresenter?>() {
    var mProduct: PurchaseProductBean? = null
    var mMachineType: String = ""

    companion object {
        fun openProductDetailActivity(
            context: Context,
            machineType: String,
            product: PurchaseProductBean
        ) {
            val intent = Intent(context, ProductDetailActivity::class.java)
            val bundle = Bundle()
            bundle.putString("machineType", machineType)
            bundle.putParcelable("product", product)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
    }

    override fun handleIntent(bundle: Bundle?) {
        super.handleIntent(bundle)
        mProduct = bundle?.getParcelable("product")
        mMachineType = bundle?.getString("machineType", "") ?: "";
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
        setTitle("商品详情")
        setContentView(R.layout.dy_activity_product_detail)
        tv_count.filters = arrayOf(NumberFilter().setDigits(0))
        setInputListener()
        setClickListener()
        bindDetail()
    }

    override fun getPresenter(): IPresenter? {
        return null
    }

    private fun setClickListener() {
        btn_less.setOnClickListener {
            val num = NumberUtils.string2BigDecimal(tv_count.text.toString())
            if (num.toInt() > 1) {
                tv_count.setText(num.subtract(BigDecimal.ONE).toPlainString())
            }
        }

        btn_add.setOnClickListener {
            val num = NumberUtils.string2BigDecimal(tv_count.text.toString())
            tv_count.setText(num.add(BigDecimal.ONE).toPlainString())
        }

        btn_submit.setOnClickListener {
            val count = tv_count.text.toString()
            if (mProduct == null) {
                showToast("请选择商品")
            } else if (count.isNullOrBlank()) {
                showToast("请输入商品数量")
            } else if (count.toInt() <= 0) {
                showToast("商品数量必须大于0")
            } else {
                ConfirmOrderActivity.openConfirmOrderActivity(
                    this,
                    mProduct?.id ?: "",
                    mProduct?.name ?: "",
                    mProduct?.img ?: "",
                    mProduct?.price ?: "",
                    tv_count.text.toString(),
                    mMachineType
                )
            }
        }
    }

    private fun setInputListener() {
        tv_count.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (mProduct != null) {
                    val num = NumberUtils.string2BigDecimal(tv_count.text.toString())
                    val price = NumberUtils.string2BigDecimal(mProduct?.price)
                    val amount = num.multiply(price)
                    tv_amount.text =
                        "¥${amount.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()}"
                }
            }

        })
    }

    private fun bindDetail() {
        iv_cover.load(mProduct?.img ?: "") {
            crossfade(true)
        }
        tv_title.text = mProduct?.name ?: "--"
        tv_detail.text = mProduct?.describe ?: "--"
        tv_price.text = if (TextUtils.isEmpty(mProduct?.price)) {
            "--"
        } else {
            "¥${NumberUtils.numberScale(mProduct?.price)}"
        }
        tv_count.setText("1")
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun onPaySuccess(event: PaySuccessEvent) {
        finish()
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }
}