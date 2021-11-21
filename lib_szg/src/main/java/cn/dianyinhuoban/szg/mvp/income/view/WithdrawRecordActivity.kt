package cn.dianyinhuoban.szg.mvp.income.view

import android.os.Bundle
import cn.dianyinhuoban.szg.R
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.ui.BaseActivity

class WithdrawRecordActivity : BaseActivity<IPresenter?>() {
    override fun getPresenter(): IPresenter? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dy_activity_trade_record)
        setTitle("提现明细")

        supportFragmentManager.beginTransaction().add(R.id.fl_container,WithdrawRecordFragment.newInstance()).commit()
    }


}