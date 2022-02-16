package cn.dianyinhuoban.szg.mvp.income.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import cn.dianyinhuoban.szg.R
import cn.dianyinhuoban.szg.mvp.bean.IncomeRecordDetailBean
import cn.dianyinhuoban.szg.mvp.bean.IntegralDetailBean
import cn.dianyinhuoban.szg.mvp.income.contract.IncomeRecordContract
import cn.dianyinhuoban.szg.mvp.income.contract.IntegralDetailContract
import cn.dianyinhuoban.szg.mvp.income.presenter.IncomeRecordPresenter
import cn.dianyinhuoban.szg.mvp.income.presenter.IntegralDetailPresenter
import com.wareroom.lib_base.ui.BaseActivity
import com.wareroom.lib_base.utils.DateTimeUtils

class IntegralDetailActivity : BaseActivity<IntegralDetailContract.Presenter>(),
    IntegralDetailContract.View {
    companion object {
        fun open(context: Context, recordID: String) {
            var intent = Intent(context, IntegralDetailActivity::class.java)
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
        mPresenter?.fetchIntegralDetail(recordID)
        setTitle("明细")
    }

    override fun getPresenter(): IntegralDetailContract.Presenter? {
        return IntegralDetailPresenter(this)
    }

    override fun bindIntegralDetail(detailBean: IntegralDetailBean?) {
        detailContainer?.removeAllViews()
        detailBean?.let {
            for (i in 1..6) {
                when (i) {
                    1 -> {
                        addItem("奖励名称", it.name ?: "--")
                    }
                    2 -> {
                        addItem("奖励金额", it.value ?: "--")
                    }
                    3 -> {
                        addItem(
                            "奖励时间", if (it.input_time.isNullOrEmpty()) {
                                "--"
                            } else {
                                DateTimeUtils.formatDate(
                                    it.input_time!!.toLong() * 1000,
                                    DateTimeUtils.PATTERN_YYYY_MM_DD_HH_MM_SS
                                )
                            }
                        )
                    }
                    4 -> {
                        addItem("商户名称", it.merchant_name ?: "--")
                    }
                    5 -> {
                        addItem("商户编号", it.customer_no ?: "--")
                    }
                    6 -> {
                        addItem("机具SN号", it.pos_sn ?: "--")
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