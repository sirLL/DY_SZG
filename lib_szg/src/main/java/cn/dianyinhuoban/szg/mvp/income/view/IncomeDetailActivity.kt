package cn.dianyinhuoban.szg.mvp.income.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import cn.dianyinhuoban.szg.R
import cn.dianyinhuoban.szg.mvp.bean.IncomeRecordDetailBean
import cn.dianyinhuoban.szg.mvp.income.contract.IncomeRecordContract
import cn.dianyinhuoban.szg.mvp.income.presenter.IncomeRecordPresenter
import com.wareroom.lib_base.ui.BaseActivity

class IncomeDetailActivity : BaseActivity<IncomeRecordContract.Presenter>(),
    IncomeRecordContract.View {
    companion object {
        fun open(context: Context, recordID: String) {
            var intent = Intent(context, IncomeDetailActivity::class.java)
            var bundle = Bundle()
            bundle.putString("recordID", recordID)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
    }

    private var detailContainer: LinearLayoutCompat? = null
    private var recordID = ""

    override fun handleIntent(bundle: Bundle?) {
        super.handleIntent(bundle)
        bundle?.let {
            recordID = it.getString("recordID", "")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dy_activity_income_detail)
        detailContainer = findViewById(R.id.ll_container)
        mPresenter?.fetchIncomeRecordDetail(recordID)
        setTitle("明细")
    }

    override fun getPresenter(): IncomeRecordContract.Presenter? {
        return IncomeRecordPresenter(this)
    }

    override fun bindIncomeRecordDetail(bean: IncomeRecordDetailBean?) {
        detailContainer?.removeAllViews()
        bean?.let {
            if (!it.type.isNullOrEmpty()) {
                when (it.type) {
                    "1", "2" -> {
                        //个人分润  团队分润
                        for (i in 1..8) {
                            when (i) {
                                1 -> {
                                    addItem("交易类型", it.cardType ?: "--")
                                }
                                2 -> {
                                    addItem("交易金额", it.transAmt ?: "--")
                                }
                                3 -> {
                                    addItem("交易时间", it.transTime ?: "--")
                                }
                                4 -> {
                                    addItem("交易商户", it.mercName ?: "--")
                                }
                                5 -> {
                                    addItem("交易商户号", it.mercId ?: "--")
                                }
                                6 -> {
                                    addItem("交易终端SN号", it.sn ?: "--")
                                }
                                7 -> {
                                    addItem("交易费率", it.rate ?: "--")
                                }
                                8 -> {
                                    addItem("分润金额", it.profit ?: "--")
                                }
                            }
                        }
                    }

                    "3" -> {
                        //激活返现
                        for (i in 1..6) {
                            when (i) {
                                1 -> {
                                    addItem("奖励名称",  "激活返现")
                                }
                                2 -> {
                                    addItem("奖励金额", it.profit ?: "--")
                                }
                                3 -> {
                                    addItem("奖励时间", it.transTime ?: "--")
                                }
                                4 -> {
                                    addItem("商户名称", it.mercName ?: "--")
                                }
                                5 -> {
                                    addItem("商户编号", it.mercId ?: "--")
                                }
                                6 -> {
                                    addItem("机具SN号", it.sn ?: "--")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun addItem(title: String, value: String) {
        val itemView = LayoutInflater.from(this)
            .inflate(R.layout.dy_item_income_detail, null, false)
        itemView.findViewById<TextView>(R.id.tv_title).text = title
        itemView.findViewById<TextView>(R.id.tv_value).text = value
        detailContainer?.addView(itemView)
    }
}