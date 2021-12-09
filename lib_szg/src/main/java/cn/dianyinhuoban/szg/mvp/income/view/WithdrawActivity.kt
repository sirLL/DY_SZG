package cn.dianyinhuoban.szg.mvp.income.view

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.*
import androidx.core.content.ContextCompat
import cn.dianyinhuoban.szg.R
import cn.dianyinhuoban.szg.bean.BalanceTypeBean
import cn.dianyinhuoban.szg.mvp.bean.BankBean
import cn.dianyinhuoban.szg.mvp.bean.PersonalBean
import cn.dianyinhuoban.szg.mvp.income.contract.WithdrawContract
import cn.dianyinhuoban.szg.mvp.income.presenter.BankCardListPresenter
import cn.dianyinhuoban.szg.mvp.income.presenter.WithdrawPresenter
import cn.dianyinhuoban.szg.util.StringUtil
import cn.dianyinhuoban.szg.widget.dialog.BaseBottomPicker
import cn.dianyinhuoban.szg.widget.dialog.PayPwdDialog
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.ui.BaseActivity
import com.wareroom.lib_base.utils.NumberUtils
import com.wareroom.lib_base.utils.filter.NumberFilter
import kotlinx.android.synthetic.main.dy_activity_withdraw.*

class WithdrawActivity : BaseActivity<WithdrawPresenter?>(), WithdrawContract.View {
    private var mCheckedBankCard: BankBean? = null
    private var mOtherBalance: Double = 0.0
    private var mActivationBalance: Double = 0.0
    private var mWithdrawTypePicker: WithdrawTypePicker? = null
    private var mBalanceType: BalanceTypeBean? = null
    private var mWithdrawBalancePicker: WithdrawBalancePicker? = null

    override fun getToolbarColor(): Int {
        return ContextCompat.getColor(WithdrawActivity@ this, R.color.dy_base_color_page_bg)
    }

    override fun getStatusBarColor(): Int {
        return R.color.dy_base_color_page_bg
    }

    override fun getPresenter(): WithdrawPresenter? {
        return WithdrawPresenter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dy_activity_withdraw)
        setTitle("提现")
        ed_amount.filters = arrayOf<InputFilter>(NumberFilter())
        setRightButtonText("提现明细") {
            startActivity(Intent(WithdrawActivity@ this, WithdrawRecordActivity::class.java))
        }
        cl_type_container.setOnClickListener {
            showWithdrawTypePicker()
        }
        tv_all.setOnClickListener {
            val balance = when (mBalanceType?.id) {
                "1" -> {
                    NumberUtils.numberScale(mOtherBalance)
                }
                "2" -> {
                    NumberUtils.numberScale(mActivationBalance)
                }
                else -> {
                    NumberUtils.numberScale(0.0)
                }
            }
            ed_amount.setText(balance)
            ed_amount.setSelection(balance.length)
        }
        btn_submit.setOnClickListener {
            PayPwdDialog(WithdrawActivity@ this)
                .setNumRand(true)
                .setInputComplete(object : PayPwdDialog.OnInputCodeListener {
                    override fun inputCodeComplete(dialog: Dialog?, verificationCode: String?) {
                        dialog?.dismiss()
                        submitWithdraw(verificationCode ?: "")
                    }

                    override fun inputCodeInput(dialog: Dialog?, verificationCode: String?) {
                    }
                })
                .show()
        }
        cl_balance_type.setOnClickListener {
            showWithdrawBalancePicker()
        }
        setupEditText()
        bindCheckedBalance(BalanceTypeBean("2", "激活返现"))

    }

    override fun onStart() {
        super.onStart()
        fetchBalance()
        fetchBankCard()
        fetchWithdrawFee("")
    }

    private fun showWithdrawBalancePicker() {
        if (mWithdrawBalancePicker == null) {
            mWithdrawBalancePicker = WithdrawBalancePicker.newInstance()
            mWithdrawBalancePicker?.setOnItemSelectedListener(object :
                BaseBottomPicker.OnItemSelectedListener<BalanceTypeBean, IPresenter?> {
                override fun onItemSelected(
                    bottomPicker: BaseBottomPicker<BalanceTypeBean, IPresenter?>,
                    t: BalanceTypeBean?,
                    position: Int
                ) {
                    bindCheckedBalance(t)
                }
            })
        }
        mWithdrawBalancePicker?.setCheckedID(mBalanceType?.id)
        mWithdrawBalancePicker?.show(supportFragmentManager, "WithdrawBalancePicker")
    }

    private fun bindCheckedBalance(balanceTypeBean: BalanceTypeBean?) {
        if (balanceTypeBean != null) {
            tv_balance_type.text = balanceTypeBean.name ?: "--"
        } else {
            tv_balance_type.text = ""
        }
        mBalanceType = balanceTypeBean
        bindBalance()
    }

    private fun showWithdrawTypePicker() {
        if (mWithdrawTypePicker == null) {
            mWithdrawTypePicker = WithdrawTypePicker.newInstance()
            mWithdrawTypePicker?.setOnItemSelectedListener(object :
                BaseBottomPicker.OnItemSelectedListener<BankBean, BankCardListPresenter> {
                override fun onItemSelected(
                    bottomPicker: BaseBottomPicker<BankBean, BankCardListPresenter>,
                    t: BankBean?,
                    position: Int
                ) {
                    bindCheckedBankCard(t)
                }
            })
        }
        mWithdrawTypePicker?.setCheckedID(mCheckedBankCard?.id)
        mWithdrawTypePicker?.show(supportFragmentManager, "WithdrawTypePicker")
    }

    private fun bindCheckedBankCard(bankBean: BankBean?) {
        if (bankBean != null) {
            tv_type.text = "${bankBean?.bankName}(${StringUtil.getBankCardEndNo(bankBean?.bankNo)})"
        } else {
            tv_type.text = ""
        }
        mCheckedBankCard = bankBean
        setSubmitButtonEnable()
    }

    private fun setupEditText() {
        ed_amount.filters = arrayOf(NumberFilter())
        ed_amount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                setSubmitButtonEnable()
            }
        })
    }

    private fun setSubmitButtonEnable() {
        val amount = ed_amount.text.toString()
        var amountDouble = 0.0
        if (amount.isNotEmpty()) {
            amountDouble = amount.toDouble()
        }
        val balance = when (mBalanceType?.id) {
            "1" -> {
                mOtherBalance
            }
            "2" -> {
                mActivationBalance
            }
            else -> {
                0.0
            }
        }
        btn_submit.isEnabled = (amountDouble > 0 && amountDouble <= balance && mBalanceType != null)
    }

    /************************************个人余额  START***********************************/
    private fun fetchBalance() {
        mPresenter?.fetchPersonalData()
    }

    override fun bindPersonalData(personalBean: PersonalBean?) {
        val total = if (TextUtils.isEmpty(personalBean?.total)) {
            0.0
        } else {
            NumberUtils.numberScale(personalBean?.total).toDouble()
        }
        mActivationBalance = if (TextUtils.isEmpty(personalBean?.personalActive)) {
            0.0
        } else {
            NumberUtils.numberScale(personalBean?.personalActive).toDouble()
        }
        val other = total - mActivationBalance
        mOtherBalance = if (other < 0) {
            0.0
        } else {
            other
        }
        bindBalance()
    }

    private fun bindBalance() {
        tv_balance.text = when (mBalanceType?.id) {
            "1" -> {
                NumberUtils.numberScale(mOtherBalance)
            }
            "2" -> {
                NumberUtils.numberScale(mActivationBalance)
            }
            else -> {
                NumberUtils.numberScale(0.0)
            }
        }
        setSubmitButtonEnable()
    }


    /************************************个人余额  END***********************************/

    /************************************提现手续费  START***********************************/
    private fun fetchWithdrawFee(amount: String) {
        mPresenter?.getWithdrawFee(amount)
    }

    override fun bindWithdrawFee(fee: String?) {
        var content = ""
        fee?.let {
            content = it.replace("\\n", "\n")
        }
        tv_tip.text = content
    }
    /************************************提现手续费  END*************************************/


    /************************************银行卡  START*************************************/
    private fun fetchBankCard() {
        mPresenter?.getBankList()
    }

    override fun onLoadBankList(bankBeanList: List<BankBean>) {
        if (mCheckedBankCard == null) {
            bankBeanList?.let {
                if (bankBeanList?.isNotEmpty()) {
                    bindCheckedBankCard(bankBeanList[0])
                }
            }
        }
    }
    /************************************银行卡  END*************************************/

    /************************************发起提现  START*************************************/
    private fun submitWithdraw(password: String) {
        mPresenter?.submitWithdraw(
            mBalanceType?.id ?: "",
            mCheckedBankCard?.id ?: "",
            NumberUtils.numberScale(ed_amount.text.toString()),
            password
        )
    }

    override fun onSubmitWithdrawSuccess() {
        showToast("发起提现成功")
//        startActivity(Intent(WithdrawActivity@ this, WithdrawSuccessActivity::class.java))
        finish()
    }

    /************************************发起提现  END*************************************/

}