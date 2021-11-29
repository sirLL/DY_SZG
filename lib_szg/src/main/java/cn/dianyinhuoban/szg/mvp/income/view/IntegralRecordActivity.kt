package cn.dianyinhuoban.szg.mvp.income.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import cn.dianyinhuoban.szg.R
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.ui.BaseActivity

class IntegralRecordActivity : BaseActivity<IPresenter?>() {
    companion object{
        fun open(context: Context,machineType:String){
            val intent=Intent(context,IntegralRecordActivity::class.java)
            val bundle=Bundle()
            bundle.putString("machineType",machineType)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dy_activity_integral_record)
        setTitle("积分记录")
        val machineType = intent.extras?.getString("machineType", "") ?: ""
        supportFragmentManager.beginTransaction().add(
            R.id.fl_container,
            IntegralRecordFragment.newInstance(machineType),
            "IntegralRecordFragment"
        ).commit()
    }

    override fun getPresenter(): IPresenter? {
        return null
    }
}