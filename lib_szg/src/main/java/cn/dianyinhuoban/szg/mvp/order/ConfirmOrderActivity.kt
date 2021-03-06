package cn.dianyinhuoban.szg.mvp.order

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import cn.dianyinhuoban.szg.R
import cn.dianyinhuoban.szg.event.PaySuccessEvent
import cn.dianyinhuoban.szg.mvp.bean.AddressBean
import cn.dianyinhuoban.szg.mvp.bean.PayTypeBean
import cn.dianyinhuoban.szg.mvp.me.presenter.MePresenter
import cn.dianyinhuoban.szg.mvp.order.contract.CreateOrderContract
import cn.dianyinhuoban.szg.mvp.order.contract.PayTypeContract
import cn.dianyinhuoban.szg.mvp.order.presenter.CreateOrderPresenter
import cn.dianyinhuoban.szg.mvp.order.view.AddressManagerActivity
import cn.dianyinhuoban.szg.mvp.order.view.PayInfoActivity
import cn.dianyinhuoban.szg.mvp.setting.view.AddShipAddressActivity
import cn.dianyinhuoban.szg.payapi.alipay.AlipayActivity
import cn.dianyinhuoban.szg.widget.dialog.BaseBottomPicker
import cn.dianyinhuoban.szg.widget.dialog.PayPwdDialog
import cn.dianyinhuoban.szg.widget.dialog.PayTypePicker
import coil.load
import com.wareroom.lib_base.ui.BaseActivity
import com.wareroom.lib_base.utils.DimensionUtils
import com.wareroom.lib_base.utils.NumberUtils
import kotlinx.android.synthetic.main.dy_activity_confirm_order.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.math.BigDecimal

class ConfirmOrderActivity : BaseActivity<CreateOrderPresenter?>(), CreateOrderContract.View {
    var mMachineType: String = ""
    var mProductName: String? = null
    var mProductImg: String? = null
    var mProductPrice: String? = null
    var mProductNum: String? = null
    var mProductID: String? = null
    var mPayTypePicker: PayTypePicker? = null

    var mPayType: PayTypeBean? = null
    var mAddress: AddressBean? = null

    companion object {
        const val RC_ADDRESS_PICKER = 1021
        const val RC_ADDRESS_EDIT = 1022
        const val RC_ADDRESS_ADD = 1023

        fun openConfirmOrderActivity(
            context: Context,
            productID: String,
            productName: String,
            productImg: String,
            productPrice: String,
            productNum: String,
            machineType: String
        ) {
            val intent = Intent(context, ConfirmOrderActivity::class.java)
            val bundle = Bundle()
            bundle.putString("productID", productID)
            bundle.putString("productName", productName)
            bundle.putString("productImg", productImg)
            bundle.putString("productPrice", productPrice)
            bundle.putString("productNum", productNum)
            bundle.putString("machineType", machineType)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
    }

    override fun handleIntent(bundle: Bundle?) {
        super.handleIntent(bundle)
        bundle?.let {
            mMachineType = it.getString("machineType", "")
            mProductName = it.getString("productName", "")
            mProductImg = it.getString("productImg", "")
            mProductPrice = it.getString("productPrice", "")
            mProductNum = it.getString("productNum", "")
            mProductID = it.getString("productID", "")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dy_activity_confirm_order)
        EventBus.getDefault().register(this)
        setTitle("????????????")
        btn_submit.setOnClickListener {
            submitOrder()
        }
        ll_pay_type_container.setOnClickListener {
            showPayTypePicker()
        }
        ll_add_address_container.setOnClickListener {
            AddShipAddressActivity.openAddShipAddressActivity(
                ConfirmOrderActivity@ this, null,
                RC_ADDRESS_ADD
            )
        }
        cl_address_container.setOnClickListener {
            AddressManagerActivity.openAddressManagerActivity(this, RC_ADDRESS_PICKER)
        }

        loadProduct()
    }

    override fun onStart() {
        super.onStart()
        fetchAddress()
    }

    private fun setSubmitEnable() {
        btn_submit.isEnabled =
            mAddress != null && !TextUtils.isEmpty(mProductID)

//        btn_submit.isEnabled =
//            mAddress != null && mPayType != null && !TextUtils.isEmpty(mProductID)
    }

    private fun fetchAddress() {
        mPresenter?.fetchAddress()
    }

    override fun bindDefAddress(addressData: List<AddressBean?>?) {
        var address: AddressBean? = null
        if (!addressData.isNullOrEmpty()) {
            addressData.forEach { addressBean ->
                if (mAddress == null || TextUtils.isEmpty(mAddress?.id)) {
                    if (addressBean != null && !addressBean.id.isNullOrBlank()) {
                        address = addressBean
                        return@forEach
                    }
                } else {
                    if (addressBean != null && !addressBean.id.isNullOrBlank() && addressBean.id == mAddress?.id) {
                        address = addressBean
                        return@forEach
                    }
                }
            }

        }
        if (address == null && mAddress != null) {
            if (!addressData.isNullOrEmpty()) {
                addressData.forEach { addressBean ->
                    if (addressBean != null && !addressBean.id.isNullOrBlank()) {
                        address = addressBean
                        return@forEach
                    }
                }
            }
        }
        bindCheckedAddress(address)
    }

    private fun showPayTypePicker() {
        if (mPayTypePicker == null) {
            mPayTypePicker = PayTypePicker.newInstance(mMachineType)
            mPayTypePicker?.setOnItemSelectedListener(object :
                BaseBottomPicker.OnItemSelectedListener<PayTypeBean?, PayTypeContract.Presenter> {
                override fun onItemSelected(
                    bottomPicker: BaseBottomPicker<PayTypeBean?, PayTypeContract.Presenter>,
                    t: PayTypeBean?,
                    position: Int,
                ) {
                    t?.let {
                        mPayTypePicker?.dismiss()
                        bindPayType(it)
                    }
                }
            })
        }
        if (!mPayTypePicker!!.isAdded) {
            mPayTypePicker?.show(supportFragmentManager, "PayTypePicker")
        }
    }

    private fun bindPayType(payTypeBean: PayTypeBean?) {
        mPayType = payTypeBean
        tv_pay_type.text = mPayType?.name ?: ""
        setSubmitEnable()
    }

    private fun loadProduct() {
        val dp2px3: Float = DimensionUtils.dp2px(this, 3).toFloat()
        iv_cover.load(mProductImg ?: "") {
            crossfade(true)
            error(R.drawable.dy_ic_app_logo)
            placeholder(R.drawable.dy_ic_app_logo)
        }

        tv_price.text = NumberUtils.formatMoney(mProductPrice)
        tv_name.text = mProductName ?: "--"
        tv_count.text = "x${mProductNum ?: "--"}"
        val num = NumberUtils.string2BigDecimal(mProductNum)
        val price = NumberUtils.string2BigDecimal(mProductPrice)
        tv_amount.text =
            "??${
                num.multiply(price).setScale(2, BigDecimal.ROUND_HALF_UP).stripTrailingZeros()
                    .toPlainString()
            }"
        tv_amount_.text =
            "??${
                num.multiply(price).setScale(2, BigDecimal.ROUND_HALF_UP).stripTrailingZeros()
                    .toPlainString()
            }"
    }

    override fun getPresenter(): CreateOrderPresenter? {
        return CreateOrderPresenter(this)
    }

    private fun submitOrder() {
        if (TextUtils.isEmpty(mProductID)) {
            showToast("?????????????????????")
            return
        }
        if (TextUtils.isEmpty(mProductNum)) {
            showToast("???????????????????????????")
            return
        }
        if (mAddress == null) {
            showToast("?????????????????????")
            return
        }
        if (mPayType == null) {
            showToast("?????????????????????")
            return
        }
        mPayType?.let {
            if (it.id == 1L || it.id == 2L) {
                showPasswordSubmitOrder()
            } else if (it.id == 5L) {
                submitOrder("")
            } else if (it.id == 6L || it.id == 7L) {
                val num = NumberUtils.string2BigDecimal(mProductNum)
                val price = NumberUtils.string2BigDecimal(mProductPrice)
                val amount =
                    num.multiply(price).setScale(2, BigDecimal.ROUND_HALF_UP).stripTrailingZeros()
                        .toPlainString()
                PayInfoActivity.openPayInfoActivity(
                    this,
                    amount,
                    mProductID ?: "",
                    num.setScale(2, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString(),
                    mAddress!!.id ?: "",
                    if (it.id == 6L) {
                        "1"
                    } else {
                        "2"
                    }
                )
            }
        }
    }

    private fun showPasswordSubmitOrder() {
        PayPwdDialog(WithdrawActivity@ this)
            .setNumRand(true)
            .setInputComplete(object : PayPwdDialog.OnInputCodeListener {
                override fun inputCodeComplete(dialog: Dialog?, verificationCode: String?) {
                    dialog?.dismiss()
                    submitOrder(verificationCode)
                }

                override fun inputCodeInput(dialog: Dialog?, verificationCode: String?) {
                }
            })
            .show()
    }

    private fun submitOrder(password: String?) {
        if (TextUtils.isEmpty(mProductID)) {
            showToast("?????????????????????")
            return
        }
        if (TextUtils.isEmpty(mProductNum)) {
            showToast("???????????????????????????")
            return
        }
        mPresenter?.submitPurchaseOrder(
            mProductID ?: "",
            mProductNum ?: "",
            mAddress?.id ?: "",
            mPayType?.id?.toString() ?: "",
            "",
            "",
            "",
            "",
            password ?: ""
        )
    }

    override fun startAlipay(payInfo: String) {
//        val intent = Intent(this, H5PayActivity::class.java)
//        val bundle = Bundle()
//        bundle.putString("url", payInfo)
//        intent.putExtras(bundle)
//        startActivity(intent)
        AlipayActivity.openAlipayActivity(this, "???????????????", payInfo)
    }

    override fun startWechatPay() {

    }

    private fun onPaySuccess() {
        EventBus.getDefault().post(PaySuccessEvent())
        showToast("????????????")
        OrderListActivity.open(this)
        finish()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun onPaySuccess(event: PaySuccessEvent) {
        finish()
    }


    override fun onSubmitOrderSuccess() {
        onPaySuccess()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                RC_ADDRESS_EDIT -> {
                    val address = data?.extras?.getParcelable<AddressBean>("address")
                    bindCheckedAddress(address)
                }

                RC_ADDRESS_PICKER -> {
                    val address = data?.extras?.getParcelable<AddressBean>("address")
                    bindCheckedAddress(address)
                }

                RC_ADDRESS_ADD -> {
                    mPresenter?.fetchAddress()
                }
            }
        }
    }

    private fun bindCheckedAddress(address: AddressBean?) {
        mAddress = address
        if (address == null) {
            cl_address_container.visibility = View.GONE
            ll_add_address_container.visibility = View.VISIBLE
        } else {
            cl_address_container.visibility = View.VISIBLE
            ll_add_address_container.visibility = View.GONE
            tv_name_.text = address.name
            tv_phone_.text = address.phone
            tv_address_.text = "${address.area}${address.address}"
        }
        setSubmitEnable()
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

}