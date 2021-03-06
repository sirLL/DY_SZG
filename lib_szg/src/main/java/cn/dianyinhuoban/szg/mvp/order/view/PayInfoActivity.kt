package cn.dianyinhuoban.szg.mvp.order.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import cn.dianyinhuoban.szg.R
import cn.dianyinhuoban.szg.event.PaySuccessEvent
import cn.dianyinhuoban.szg.mvp.PreviewImageActivity
import cn.dianyinhuoban.szg.mvp.bean.OfflinePayInfoBean
import cn.dianyinhuoban.szg.mvp.bean.UploadResultBean
import cn.dianyinhuoban.szg.mvp.order.OrderListActivity
import cn.dianyinhuoban.szg.mvp.order.contract.OfflinePayContract
import cn.dianyinhuoban.szg.mvp.order.presenter.OfflinePayPresenter
import cn.dianyinhuoban.szg.util.CoilEngine
import cn.dianyinhuoban.szg.util.StringUtil
import cn.dianyinhuoban.szg.widget.dialog.ImageDialog
import cn.dianyinhuoban.szg.widget.dialog.MessageDialog
import coil.load
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.github.gzuliyujiang.wheelpicker.OptionPicker
import com.github.gzuliyujiang.wheelview.contract.TextProvider
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import com.tbruyelle.rxpermissions2.RxPermissions
import com.wareroom.lib_base.ui.BaseActivity
import com.wareroom.lib_base.utils.BitmapUtils
import com.wareroom.lib_base.utils.NumberUtils
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus
import java.io.File
import java.util.*

class PayInfoActivity : BaseActivity<OfflinePayContract.Presenter?>(), OfflinePayContract.View {
    companion object {
        fun openPayInfoActivity(
            context: Context,
            amount: String,
            productID: String,
            productNum: String,
            addressID: String,
            payMethod: String
        ) {
            val intent = Intent(context, PayInfoActivity::class.java)
            val bundle = Bundle()
            bundle.putString("productID", productID)
            bundle.putString("productNum", productNum)
            bundle.putString("addressID", addressID)
            bundle.putString("amount", amount)
            bundle.putString("payMethod", payMethod)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
    }

    private var amount: String = ""
    private var productID: String = ""
    private var productNum: String = ""
    private var addressID: String = ""
    private var voucherURL: String? = null
    private var payMethod: String? = null
    private var qrImgUrl: String? = null

    private var tvAmount: TextView? = null
    private var tvName: TextView? = null
    private var tvAccount: TextView? = null
    private var tvBankName: TextView? = null
    private var edPayName: EditText? = null
    private var edPayAccount: EditText? = null
    private var edPayBank: EditText? = null
    private var ivPayInfo: ImageView? = null
    private var btnSubmit: Button? = null
    private var tvPayType: TextView? = null
    private var tvQRTitle: TextView? = null
    private var ivQR: ImageView? = null
    private var payTypeContainer: LinearLayout? = null
    private var voucherContainer: LinearLayout? = null
    private var alipayContainer: LinearLayout? = null
    private var bankContainer: LinearLayout? = null

//    private val qrDialog by lazy {
//        ImageDialog(this)
//    }

    override fun handleIntent(bundle: Bundle?) {
        super.handleIntent(bundle)
        amount = bundle?.getString("amount", "") ?: ""
        productID = bundle?.getString("productID", "") ?: ""
        productNum = bundle?.getString("productNum", "") ?: ""
        addressID = bundle?.getString("addressID", "") ?: ""
        payMethod = bundle?.getString("payMethod", "") ?: ""
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
        tvPayType = findViewById(R.id.tv_pay_type)
        tvQRTitle = findViewById(R.id.tv_qr)
        ivQR = findViewById(R.id.iv_qr)
        payTypeContainer = findViewById(R.id.ll_pay_type_container)
        voucherContainer = findViewById(R.id.ll_voucher_container)
        alipayContainer = findViewById(R.id.ll_qr_container)
        bankContainer = findViewById(R.id.ll_bank_container)

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
        val qrTitle =
            "<font color=\"#999999\">??????????????????</font><font color=\"#C50018\">(???????????????????????????)</font>"
        tvQRTitle?.text = Html.fromHtml(qrTitle)

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

        ivQR?.setOnClickListener {
//            qrDialog.show()
            qrImgUrl?.let {url->
                PreviewImageActivity.open(this, arrayOf(url))
            }
        }
        ivQR?.setOnLongClickListener { v ->
            v?.let {
                saveView(it)
            }
            false
        }

        tvAmount?.text = "${NumberUtils.numberScale(amount)}???"

        showPayTypeUI()
        mPresenter?.fetchOfflinePayInfo()
    }

    override fun getPresenter(): OfflinePayContract.Presenter? {
        return OfflinePayPresenter(this)
    }

    private fun copyText(text: String) {
        StringUtil.copyString(this, text)
        showToast("????????????")
    }

    override fun bindOfflinePayInfo(payInfoBean: OfflinePayInfoBean?) {
        if (payMethod == "1") {
            tvName?.text = payInfoBean?.name ?: ""
            tvAccount?.text = payInfoBean?.bankNo ?: ""
            tvBankName?.text = payInfoBean?.bankName ?: ""
        } else if (payMethod == "2") {
            ivQR?.let {
                Glide.with(this)
                    .asBitmap()
                    .load(payInfoBean?.aliPay ?: "")
                    .into(object : SimpleTarget<Bitmap>() {
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap>?
                        ) {
                            it.setImageBitmap(resource)
                        }
                    })
            }
            qrImgUrl = payInfoBean?.aliPay
//            qrDialog.setImagePath(payInfoBean?.aliPay)
        }
    }

    private fun showPayTypeUI() {
        setTitle(
            when (payMethod) {
                "1" -> {
                    "????????????"
                }
                "2" -> {
                    "????????????"
                }
                else -> {
                    ""
                }
            }
        )

        when (payMethod) {
            "1" -> {
                //?????????
                bankContainer?.visibility = View.VISIBLE
                alipayContainer?.visibility = View.GONE
                voucherContainer?.visibility = View.VISIBLE
            }
            "2" -> {
                //???????????????
                bankContainer?.visibility = View.GONE
                alipayContainer?.visibility = View.VISIBLE
                voucherContainer?.visibility = View.VISIBLE
            }
            else -> {
                bankContainer?.visibility = View.GONE
                alipayContainer?.visibility = View.GONE
                voucherContainer?.visibility = View.GONE
                tvPayType?.text = "--"
            }
        }
        setSubmitEnable()
    }

    private fun showImageDialog() {
        val selectItems = arrayOf("??????", "??????")
        AlertDialog.Builder(PayInfoActivity@ this)
            .setTitle("????????????")
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
        when (payMethod) {
            "1" -> {
                btnSubmit?.isEnabled =
                    !amount.isNullOrBlank() && !productID.isNullOrBlank() && !productNum.isNullOrBlank()
                            && !name.isNullOrBlank() && name.length >= 2
                            && !no.isNullOrBlank() && no.length >= 5
                            && !bank.isNullOrBlank() && bank.length >= 2
                            && !voucherURL.isNullOrBlank()
            }
            "2" -> {
                btnSubmit?.isEnabled =
                    !amount.isNullOrBlank() && !productID.isNullOrBlank() && !productNum.isNullOrBlank()
                            && !voucherURL.isNullOrBlank()
            }
            else -> {
                btnSubmit?.isEnabled = false
            }
        }

    }

    private fun submitOrder() {
        MessageDialog(this)
            .setTitle("????????????")
            .setMessage("???????????????????????????")
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
            showToast("??????????????????????????????????????????")
            return
        }
        if (productNum.isNullOrBlank()) {
            showToast("???????????????????????????")
            return
        }
        if (addressID.isNullOrBlank()) {
            showToast("???????????????????????????")
            return
        }
        if (payMethod.isNullOrBlank()) {
            showToast("?????????????????????")
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
            "",
            payMethod!!
        )
    }

    override fun onSubmitOrderSuccess() {
        EventBus.getDefault().post(PaySuccessEvent())
        showToast("??????????????????")
        OrderListActivity.open(this)
        finish()
    }

    private fun saveView(view: View) {
        RxPermissions(PayInfoActivity@ this).request(
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).subscribe { aBoolean: Boolean ->
            if (aBoolean) {
                saveView2File(view)
            } else {
                showToast("???????????????????????????")
            }
        }
    }

    private fun saveView2File(view: View) {
        showLoading()
        Observable.just(view)
            .map { v ->
                val fileName = "DYHM${Calendar.getInstance().timeInMillis}.jpg"
                BitmapUtils.saveBitmap(
                    PayInfoActivity@ this,
                    BitmapUtils.view2Bitmap(v),
                    fileName
                )
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<String> {
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(path: String) {
                    hideLoading()
                    showToast("???????????????${path}")
                }

                override fun onError(e: Throwable) {
                    hideLoading()
                    showToast("??????????????????")
                }

                override fun onComplete() {}
            })
    }

}