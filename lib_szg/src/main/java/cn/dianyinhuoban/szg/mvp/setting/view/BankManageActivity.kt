package cn.dianyinhuoban.szg.mvp.setting.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import cn.dianyinhuoban.szg.R
import cn.dianyinhuoban.szg.mvp.bean.BankBean
import cn.dianyinhuoban.szg.mvp.bean.ImageCodeBean
import cn.dianyinhuoban.szg.mvp.me.view.BindBankCardActivity
import cn.dianyinhuoban.szg.mvp.setting.contract.BankContract
import cn.dianyinhuoban.szg.mvp.setting.presenter.BankPresenter
import cn.dianyinhuoban.szg.widget.dialog.MessageDialog
import com.wareroom.lib_base.ui.BaseActivity
import com.wareroom.lib_base.ui.adapter.SimpleAdapter
import kotlinx.android.synthetic.main.dy_activity_bank_manage.*
import kotlinx.android.synthetic.main.dy_item_bank.view.*

class BankManageActivity : BaseActivity<BankPresenter?>(), BankContract.View {

    companion object {

        const val TYPE_DEL = "1"
        const val TYPE_DEFAULT = "2";
        const val REQ_ADD = 100;
        const val REQ_UPDATE = 200;

    }

    override fun getPresenter(): BankPresenter? {
        return BankPresenter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dy_activity_bank_manage)
        setTitle("银行卡管理")

        rv_bank_list.adapter = object : SimpleAdapter<BankBean>(R.layout.dy_item_bank) {
            override fun convert(
                viewHolder: SimpleViewHolder?,
                position: Int,
                itemData: BankBean?
            ) {
                viewHolder?.itemView?.tv_bank_name2?.text = itemData?.bankName
                viewHolder?.itemView?.tv_bank_number2?.text = itemData?.bankNo

                if (itemData?.isDefault == 1) {
                    //当前不是默认卡
                    viewHolder?.itemView?.tv_bank_default?.visibility = View.VISIBLE
                    viewHolder?.itemView?.img_bank_default?.visibility = View.GONE

                } else {
                    viewHolder?.itemView?.tv_bank_default?.visibility = View.GONE
                    viewHolder?.itemView?.img_bank_default?.visibility = View.VISIBLE
                }

                viewHolder?.itemView?.tv_bank_del?.setOnClickListener {
                    var dialog = MessageDialog(this@BankManageActivity)
                    dialog.setTitle("提醒")
                    dialog.setMessage("你是否删除")
                        .setOnConfirmClickListener {
                            it.dismiss()
                            mPresenter?.setBank(TYPE_DEL, itemData?.id.toString())
                        }
                    dialog.show()
                }

                viewHolder?.itemView?.tv_bank_default?.setOnClickListener {
                    var dialog = MessageDialog(this@BankManageActivity)
                    dialog.setTitle("提醒")
                    dialog.setMessage("你是否设置为默认")
                        .setOnConfirmClickListener {
                            it.dismiss()
                            mPresenter?.setBank(TYPE_DEFAULT, itemData?.id.toString())
                        }
                    dialog.show()
                }

            }

            override fun onItemClick(data: BankBean?, position: Int) {
                data?.let {
                    BindBankCardActivity.open(this@BankManageActivity, it, REQ_UPDATE)
                }
            }

        }

        el_add_bank.setOnClickListener {
            BindBankCardActivity.open(BankManageActivity@ this, REQ_ADD)
        }

        loadData()
    }

    private fun loadData() {
        mPresenter?.getBankList()
    }

    override fun onLoadBankList(bankBeanList: List<BankBean>) {
        super.onLoadBankList(bankBeanList)
        if (bankBeanList.isEmpty()) {
            tv_empty.visibility = View.VISIBLE
        } else {
            tv_empty.visibility = View.GONE
        }

        (rv_bank_list.adapter as SimpleAdapter<BankBean>).data = bankBeanList
    }


    override fun onSetSuccess() {
        super.onSetSuccess()
        loadData()
    }

    override fun showImageCode(imageCode: ImageCodeBean?) {

    }

    override fun startSMSCountdown() {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQ_ADD -> {
                    loadData()
                }
                REQ_UPDATE -> {
                    loadData()
                }

            }
        }
    }
}