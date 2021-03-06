package cn.dianyinhuoban.szg.mvp.income.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import cn.dianyinhuoban.szg.R
import cn.dianyinhuoban.szg.mvp.bean.TabEntity
import com.dy.tablayout.listener.CustomTabEntity
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.ui.BaseActivity
import kotlinx.android.synthetic.main.dy_activity_income_purchase_detail.*
import java.util.*


class IncomePurchaseDetailActivity : BaseActivity<IPresenter?>() {

    override fun getPresenter(): IPresenter? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dy_activity_income_purchase_detail)
        setTitle("采购奖励明细")
        setupTab()
    }

    private fun setupTab() {
        val tabData = ArrayList<CustomTabEntity>()
        tabData.add(TabEntity("日数据"))
        tabData.add(TabEntity("月数据"))
        val fragmentData = ArrayList<Fragment>()
        fragmentData.add(IncomePurchaseDetailFragment.newInstance(IncomePurchaseDetailFragment.DATA_TYPE_DAY))
        fragmentData.add(IncomePurchaseDetailFragment.newInstance(IncomePurchaseDetailFragment.DATA_TYPE_MONTH))
        tab_layout.setTabData(tabData, supportFragmentManager, R.id.fl_container, fragmentData)
    }


}