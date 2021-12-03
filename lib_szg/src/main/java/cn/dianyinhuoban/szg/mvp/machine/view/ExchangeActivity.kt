package cn.dianyinhuoban.szg.mvp.machine.view
import android.os.Bundle
import cn.dianyinhuoban.szg.R
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.ui.BaseActivity

class ExchangeActivity : BaseActivity<IPresenter?>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dy_activity_exchange)

        setTitle("积分兑换")
        supportFragmentManager?.beginTransaction()
            .add(R.id.fl_container, ExchangeFragment.newInstance(), "ExchangeFragment").commit()
    }

    override fun getPresenter(): IPresenter? {
        return null
    }
}