package cn.dianyinhuoban.szg.mvp.me.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import cn.dianyinhuoban.szg.CountdownTextUtils
import cn.dianyinhuoban.szg.R
import cn.dianyinhuoban.szg.mvp.bean.BankBean
import cn.dianyinhuoban.szg.mvp.bean.ImageCodeBean
import cn.dianyinhuoban.szg.mvp.setting.contract.BankContract
import cn.dianyinhuoban.szg.mvp.setting.presenter.BankPresenter
import cn.dianyinhuoban.szg.widget.dialog.ImageCodeDialog
import com.wareroom.lib_base.ui.BaseActivity
import com.wareroom.lib_base.utils.ValidatorUtils
import kotlinx.android.synthetic.main.dy_activity_bind_bank_card.*
import kotlinx.android.synthetic.main.dy_activity_bind_bank_card.btn_submit
import kotlinx.android.synthetic.main.dy_activity_bind_bank_card.ed_phone
import kotlinx.android.synthetic.main.dy_activity_register.*

class BindBankCardActivity : BaseActivity<BankPresenter?>(), BankContract.View {

    companion object {
        fun open(context: Context) {
            var intent = Intent(context, BindBankCardActivity::class.java)
            context.startActivity(intent)
        }

        fun open(context: Activity, requestCode: Int) {
            var intent = Intent(context, BindBankCardActivity::class.java)
            context.startActivityForResult(intent, requestCode)
        }

        fun open(context: Activity, bankCard: BankBean, requestCode: Int) {
            val intent = Intent(context, BindBankCardActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable("bankCard", bankCard)
            intent.putExtras(bundle)
            context.startActivityForResult(intent, requestCode)
        }
    }

    private var mBankCard: BankBean? = null

    private val mImageCodeDialog: ImageCodeDialog by lazy {
        ImageCodeDialog.newInstance()
    }

    override fun getPresenter(): BankPresenter? {
        return BankPresenter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (mBankCard == null) {
            setTitle("???????????????")
        } else {
            setTitle("???????????????")
        }
        setContentView(R.layout.dy_activity_bind_bank_card)
        bindBankCard()
        btn_submit.setOnClickListener {
            val bankUsername = ed_name.text.toString().trim()
            val bankName = ed_bank.text.toString().trim();
            val bankNo = ed_card_no.text.toString().trim();
            val phoneNumber = ed_phone.text.toString().trim();
            val phoneCode = ed_code.text.toString().trim();

            if (bankUsername.isEmpty()) {
                showToast("???????????????")
                return@setOnClickListener
            }

            if (bankName.isEmpty()) {
                showToast("??????????????????")
                return@setOnClickListener
            }

            if (bankNo.isEmpty()) {
                showToast("?????????????????????")
                return@setOnClickListener
            }

            if (phoneNumber.isEmpty()) {
                showToast("????????????????????????????????????")
                return@setOnClickListener
            }
//
//            if (phoneCode.isEmpty()) {
//                showToast("??????????????????")
//                return@setOnClickListener
//            }

            if (mBankCard == null || mBankCard?.id.isNullOrBlank()) {
                mPresenter?.addBank(bankUsername, bankName, bankNo, phoneNumber, phoneCode)
            } else {
                mPresenter?.updateBank(
                    bankUsername,
                    bankName,
                    bankNo,
                    phoneNumber,
                    phoneCode,
                    mBankCard?.id ?: ""
                )
            }

        }

        tv_get_code.setOnClickListener {
            fetchImageCode()
        }

    }

    private fun bindBankCard() {
        mBankCard?.let {
            val bankName = it.bankName
            val bankCardNo = it.bankNo
            val name = it.name
            bankName?.let {
                ed_bank.setText(bankName)
                ed_bank.setSelection(bankName.length)
            }
            bankCardNo?.let {
                ed_card_no.setText(bankCardNo)
                ed_card_no.setSelection(bankCardNo.length)
            }
            name?.let {
                ed_name.setText(name)
                ed_name.setSelection(name.length)
            }
        }
    }

    override fun handleIntent(bundle: Bundle?) {
        super.handleIntent(bundle)
        mBankCard = bundle?.getParcelable("bankCard")
    }

    override fun showImageCode(imageCode: ImageCodeBean?) {
        imageCode?.let {
            if (!mImageCodeDialog.isAdded) {
                mImageCodeDialog.show(supportFragmentManager, "ImageCodeDialog")
                mImageCodeDialog.setImageCodeCallBack(object : ImageCodeDialog.ImageCodeCallBack {
                    override fun getImageCode(code: String?, inputCode: String) {
                        if (code != null) {
                            sendSMS(inputCode, code)
                        }
                    }

                    override fun changeImage() {
                        fetchImageCode()
                    }
                })
            }
            mImageCodeDialog.loadImageCode(it.code, it.img)
        }
    }

    private fun fetchImageCode() {
        val phone = ed_phone.text.toString()
        if (phone.isEmpty()) {
            showToast("?????????????????????")
            return
        }
        if (!ValidatorUtils.isMobile(phone)) {
            showToast("???????????????????????????")
            return
        }

        mPresenter?.fetchImageCode()
    }

    private fun sendSMS(imageCode: String, imageKey: String) {
        val phone = ed_phone.text.toString()
        if (phone.isEmpty()) {
            showToast("?????????????????????")
            return
        }
        if (!ValidatorUtils.isMobile(phone)) {
            showToast("???????????????????????????")
            return
        }
        mPresenter?.let {
            it.onSendSMS(phone, imageKey, imageCode)
        }
    }


    override fun startSMSCountdown() {
        CountdownTextUtils(this).startCountdown(tv_get_code, "???????????????")
    }

    override fun onUpdateBankSuccess() {
        super.onUpdateBankSuccess()
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun onAddBankSuccess() {
        super.onAddBankSuccess()
        setResult(Activity.RESULT_OK)
        finish()
    }
}