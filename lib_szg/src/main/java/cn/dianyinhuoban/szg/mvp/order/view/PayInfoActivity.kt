package cn.dianyinhuoban.szg.mvp.order.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import cn.dianyinhuoban.szg.R
import cn.dianyinhuoban.szg.event.PaySuccessEvent
import cn.dianyinhuoban.szg.mvp.bean.OfflinePayInfoBean
import cn.dianyinhuoban.szg.mvp.bean.UploadResultBean
import cn.dianyinhuoban.szg.mvp.order.OrderListActivity
import cn.dianyinhuoban.szg.mvp.order.contract.OfflinePayContract
import cn.dianyinhuoban.szg.mvp.order.presenter.OfflinePayPresenter
import cn.dianyinhuoban.szg.util.CoilEngine
import cn.dianyinhuoban.szg.util.StringUtil
import cn.dianyinhuoban.szg.widget.dialog.MessageDialog
import coil.load
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import com.wareroom.lib_base.ui.BaseActivity
import com.wareroom.lib_base.utils.NumberUtils
import org.greenrobot.eventbus.EventBus
import java.io.File

class PayInfoActivity : BaseActivity<OfflinePayContract.Presenter?>(), OfflinePayContract.View {
    companion object {
        fun openPayInfoActivity(
            context: Context,
            amount: String,
            productID: String,
            productNum: String,
            addressID: String,
            payType: String
        ) {
            val intent = Intent(context, PayInfoActivity::class.java)
            val bundle = Bundle()
            bundle.putString("productID", productID)
            bundle.putString("productNum", productNum)
            bundle.putString("addressID", addressID)
            bundle.putString("payType", payType)
            bundle.putString("amount", amount)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
    }

    private var amount: String = ""
    private var productID: String = ""
    private var productNum: String = ""
    private var addressID: String = ""
    private var payType: String = ""
    private var voucherURL: String? = null

    private var tvAmount: TextView? = null
    private var tvName: TextView? = null
    private var tvAccount: TextView? = null
    private var tvBankName: TextView? = null
    private var edPayName: EditText? = null
    private var edPayAccount: EditText? = null
    private var edPayBank: EditText? = null
    private var ivPayInfo: ImageView? = null
    private var btnSubmit: Button? = null

    override fun handleIntent(bundle: Bundle?) {
        super.handleIntent(bundle)
        amount = bundle?.getString("amount", "") ?: ""
        productID = bundle?.getString("productID", "") ?: ""
        productNum = bundle?.getString("productNum", "") ?: ""
        addressID = bundle?.getString("addressID", "") ?: ""
        payType = bundle?.getString("payType", "") ?: ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dy_activity_pay_info)
        tvAmount = findViewById(R.id.tv_amount)
        tvName = findViewById(R.id.tv_name)
        tvAccount = findViewById(R.id.tv_account)
        tvBankName = findViewById(R.id.tv_bank)
        edPayName = findViewById(R.id.ed_pay_name)
        edPayAccount = findViewById(R.id.ed_pay_account)
        edPayBank = findViewById(R.id.ed_pay_bank)
        ivPayInfo = findViewById(R.id.iv_pay_info)
        btnSubmit = findViewById(R.id.btn_submit)

        findViewById<TextView>(R.id.tv_copy_name).setOnClickListener {
            copyText(tvName?.text?.toString() ?: "")
        }
        findViewById<TextView>(R.id.tv_copy_account).setOnClickListener {
            copyText(tvAccount?.text?.toString() ?: "")
        }
        findViewById<TextView>(R.id.tv_copy_bank).setOnClickListener {
            copyText(tvBankName?.text?.toString() ?: "")
        }
        ivPayInfo?.setOnClickListener {
            showImageDialog()
        }
        btnSubmit?.setOnClickListener {
            submitOrder()
        }
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                setSubmitEnable()
            }

        }
        edPayName?.addTextChangedListener(textWatcher)
        edPayAccount?.addTextChangedListener(textWatcher)
        edPayBank?.addTextChangedListener(textWatcher)

        tvAmount?.text = "${NumberUtils.numberScale(amount)}元"
        mPresenter?.fetchOfflinePayInfo()
    }

    override fun getPresenter(): OfflinePayContract.Presenter? {
        return OfflinePayPresenter(this)
    }

    private fun copyText(text: String) {
        StringUtil.copyString(this, text)
        showToast("复制成功")
    }

    override fun bindOfflinePayInfo(payInfoBean: OfflinePayInfoBean?) {
        tvName?.text = payInfoBean?.name ?: ""
        tvAccount?.text = payInfoBean?.bankNo ?: ""
        tvBankName?.text = payInfoBean?.bankName ?: ""
    }


    private fun showImageDialog() {
        val selectItems = arrayOf("相册", "拍照")
        AlertDialog.Builder(PayInfoActivity@ this)
            .setTitle("选择照片")
            .setItems(
                selectItems
            ) { _, which ->
                when (which) {
                    0 -> {
                        openGallery()
                    }

                    1 -> {
                        openCamera()
                    }
                }
            }.create().show()
    }

    private fun openGallery() {
        PictureSelector
            .create(PayInfoActivity@ this)
            .openGallery(PictureMimeType.ofImage())
            .isEnableCrop(false)
            .selectionMode(PictureConfig.SINGLE)
            .isCompress(true)
            .imageEngine(CoilEngine.instance)
            .forResult(object : OnResultCallbackListener<LocalMedia> {
                override fun onResult(result: MutableList<LocalMedia>?) {
                    if (!result.isNullOrEmpty()) {
                        handleLocalMedia(result[0])
                    }
                }

                override fun onCancel() {

                }
            })
    }

    private fun openCamera() {
        PictureSelector
            .create(PayInfoActivity@ this)
            .openCamera(PictureMimeType.ofImage())
            .isEnableCrop(false)
            .selectionMode(PictureConfig.SINGLE)
            .isCompress(true)
            .imageEngine(CoilEngine.instance)
            .forResult(object : OnResultCallbackListener<LocalMedia> {
                override fun onResult(result: MutableList<LocalMedia>?) {
                    if (!result.isNullOrEmpty()) {
                        handleLocalMedia(result[0])
                    }
                }

                override fun onCancel() {

                }

            })
    }

    private fun handleLocalMedia(localMedia: LocalMedia) {
        val path = if (localMedia.compressPath.isNullOrBlank()) {
            localMedia.path
        } else {
            localMedia.compressPath
        }
        mPresenter?.uploadVoucher(File(path))
    }

    override fun bindVoucher(resultBean: UploadResultBean) {
        voucherURL = resultBean.url
        ivPayInfo?.load(resultBean.url) {
            crossfade(true)
        }

        setSubmitEnable()
    }

    private fun setSubmitEnable() {
        val name = edPayName?.text?.toString()
        val no = edPayAccount?.text?.toString()
        val bank = edPayBank?.text?.toString()
        btnSubmit?.isEnabled =
            !amount.isNullOrBlank() && !productID.isNullOrBlank() && !productNum.isNullOrBlank()
                    && !name.isNullOrBlank() && name.length >= 2
                    && !no.isNullOrBlank() && no.length >= 5
                    && !bank.isNullOrBlank() && bank.length >= 2
                    && !voucherURL.isNullOrBlank()
    }

    private fun submitOrder() {
        MessageDialog(this)
            .setTitle("确认订单")
            .setMessage("是否确认提交订单？")
            .setOnConfirmClickListener {
                it.dismiss()
                submitPurchaseOrder()
            }.show()
    }

    private fun submitPurchaseOrder() {
        val name = edPayName?.text?.toString()
        val no = edPayAccount?.text?.toString()
        val bank = edPayBank?.text?.toString()
        if (amount.isNullOrBlank() || productID.isNullOrBlank()) {
            showToast("缺少机具信息，请重新选择机具")
            return
        }
        if (productNum.isNullOrBlank()) {
            showToast("请重新选择机具数量")
            return
        }
        if (addressID.isNullOrBlank()) {
            showToast("请重新选择收货地址")
            return
        }
        mPresenter?.submitPurchaseOrder(
            productID,
            productNum,
            addressID,
            name ?: "",
            no ?: "",
            bank ?: "",
            voucherURL ?: "",
            ""
        )
    }

    override fun onSubmitOrderSuccess() {
        EventBus.getDefault().post(PaySuccessEvent())
        showToast("订单提交成功")
        OrderListActivity.open(this)
        finish()
    }


}