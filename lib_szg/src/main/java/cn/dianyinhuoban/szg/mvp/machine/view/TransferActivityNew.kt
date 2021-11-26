package cn.dianyinhuoban.szg.mvp.machine.view

import android.os.Bundle
import cn.dianyinhuoban.szg.R
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.ui.BaseActivity

class TransferActivityNew : BaseActivity<IPresenter?>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dy_activity_transfer_new)
    }

    override fun getPresenter(): IPresenter? {
        return null
    }
}