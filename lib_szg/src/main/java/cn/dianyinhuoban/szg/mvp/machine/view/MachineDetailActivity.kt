package cn.dianyinhuoban.szg.mvp.machine.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import cn.dianyinhuoban.szg.R
import cn.dianyinhuoban.szg.mvp.bean.MachineItemData
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.ui.BaseActivity

class MachineDetailActivity : BaseActivity<IPresenter?>() {
    companion object {
        fun openMachineDetailActivity(context: Context, machine: MachineItemData, status: String) {
            val intent = Intent(context, MachineDetailActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable("machine", machine)
            bundle.putString("status", status)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
    }

    private var machine: MachineItemData? = null
    private var status: String? = null

    private var tvType: TextView? = null
    private var tvSN: TextView? = null
    private var tvHoldDate: TextView? = null
    private var tvStatus: TextView? = null
    private var tvISExchange: TextView? = null
    private var tvEndDate: TextView? = null

    override fun handleIntent(bundle: Bundle?) {
        super.handleIntent(bundle)
        machine = bundle?.getParcelable("machine")
        status = bundle?.getString("status", "")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dy_activity_machine_detail)
        tvType = findViewById(R.id.tv_type)
        tvSN = findViewById(R.id.tv_sn)
        tvHoldDate = findViewById(R.id.tv_hold_date)
        tvStatus = findViewById(R.id.tv_status)
        tvISExchange = findViewById(R.id.tv_exchange)
        tvEndDate = findViewById(R.id.tv_end_date)

        tvType?.text = machine?.name ?: "--"
        tvSN?.text = machine?.pos_sn ?: "--"
        tvHoldDate?.text = machine?.add_time ?: "--"
        tvStatus?.text = when (status) {
            "1" -> {
                "未绑定"
            }
            "2" -> {
                "未激活"
            }
            "3" -> {
                "已激活"
            }
            else -> {
                if ("3" == machine?.act_status) {
                    "激活已返现"
                } else {
                    "--"
                }
            }
        }
        tvISExchange?.text = if (machine?.buyType == "2") {
            "是"
        } else {
            "否"
        }
        tvEndDate?.text = if (machine?.back_time.isNullOrBlank()) {
            "--"
        } else {
            machine?.back_time
        }
    }

    override fun getPresenter(): IPresenter? {
        return null
    }


}