package cn.dianyinhuoban.szg.mvp.me.view

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import cn.dianyinhuoban.szg.R
import cn.dianyinhuoban.szg.mvp.bean.AuthResult
import cn.dianyinhuoban.szg.mvp.bean.IntegralBalanceBean
import cn.dianyinhuoban.szg.mvp.bean.MeMenuBean
import cn.dianyinhuoban.szg.mvp.bean.PersonalBean
import cn.dianyinhuoban.szg.mvp.income.view.IncomeActivity
import cn.dianyinhuoban.szg.mvp.income.view.IntegralRecordActivity
import cn.dianyinhuoban.szg.mvp.income.view.WithdrawActivity
import cn.dianyinhuoban.szg.mvp.machine.view.ExchangeActivity
import cn.dianyinhuoban.szg.mvp.machine.view.MachineManagerActivity
import cn.dianyinhuoban.szg.mvp.me.contract.MeContract
import cn.dianyinhuoban.szg.mvp.me.presenter.MePresenter
import cn.dianyinhuoban.szg.mvp.me.view.adapter.MeMenuAdapter
import cn.dianyinhuoban.szg.mvp.me.view.adapter.MeMenuAdapter.OnMenuClickListener
import cn.dianyinhuoban.szg.mvp.order.OrderListActivity
import cn.dianyinhuoban.szg.mvp.order.view.ProductListActivity
import cn.dianyinhuoban.szg.mvp.pk.view.PkActivity
import cn.dianyinhuoban.szg.mvp.setting.view.*
import cn.dianyinhuoban.szg.qiyu.QYHelper
import cn.dianyinhuoban.szg.widget.dialog.MessageDialog
import coil.load
import coil.transform.CircleCropTransformation
import com.wareroom.lib_base.ui.BaseFragment
import com.wareroom.lib_base.utils.NumberUtils
import com.wareroom.lib_base.utils.OSUtils
import kotlinx.android.synthetic.main.dy_activity_home.*
import kotlinx.android.synthetic.main.dy_fragment_me.*
import java.util.*


class MeFragment : BaseFragment<MePresenter?>(), MeContract.View {
    private var mAdapter: MeMenuAdapter? = null

    companion object {
        fun newInstance(): MeFragment {
            val args = Bundle()
            val fragment = MeFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getPresenter(): MePresenter? {
        return MePresenter(this)
    }

    override fun getContentView(): Int {
        return R.layout.dy_fragment_me
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        loadMenuData()

        //收益
        view_income_action.setOnClickListener {
            startActivity(Intent(context, IncomeActivity::class.java))
        }
        //提现
        tv_withdraw.setOnClickListener {
            startActivity(Intent(context, WithdrawActivity::class.java))
        }
        //我的客户
        cl_my_client.setOnClickListener {
            startActivity(Intent(context, MyClientActivity::class.java))
        }
        //我的成员
        cl_member.setOnClickListener {
            startActivity(Intent(context, TeamActivity::class.java))
        }

        iv_message.setOnClickListener {
            startActivity(Intent(context, MessageActivity::class.java))
        }
        cl_my_parent.setOnClickListener {
            val name = tv_parent_name.text
            val phone = tv_parent_phone.text.toString()
            if (!TextUtils.isEmpty(phone)) {
                MessageDialog(requireContext())
                    .setMessage("是否拨打${name}电话?")
                    .setOnConfirmClickListener {
                        it.dismiss()
                        OSUtils.callPhone(requireContext(), phone)
                    }.show()
            }

        }
        tv_integral_left_title?.setOnClickListener {
            val tag: String = it.getTag(R.id.dy_tv_tag) as String
            if (!tag.isNullOrBlank()) {
                IntegralRecordActivity.open(requireContext(), tag)
            }
        }
        tv_integral_right_title?.setOnClickListener {
            val tag: String = it.getTag(R.id.dy_tv_tag) as String
            if (!tag.isNullOrBlank()) {
                IntegralRecordActivity.open(requireContext(), tag)
            }
        }
    }

    private fun setupRecyclerView() {
        mAdapter = MeMenuAdapter()
        recycler_view.adapter = mAdapter
        mAdapter?.onMenuClickListener = object : OnMenuClickListener {
            override fun onMenuClick(menuBean: MeMenuBean) {
                when (menuBean.id) {
                    1 -> {//机具划拨
                        startActivity(Intent(context, MachineManagerActivity::class.java))
                    }
                    2 -> {//机具采购
                        startActivity(Intent(context, ProductListActivity::class.java))
                    }
                    3 -> {//讲武堂
                        startActivity(Intent(context, JiangWuTangActivity::class.java))
                    }
                    4 -> {//采购订单
                        startActivity(Intent(context, ExchangeActivity::class.java))
                    }
                    5 -> {//银行卡
                        startActivity(Intent(context, BankManageActivity::class.java))
                    }
                    6 -> {//PK
                        startActivity(Intent(context, PkActivity::class.java))
                    }
                    7 -> {//授权书
                        startActivity(Intent(context, AuthActivity::class.java))
                    }
                    8 -> {//在线客服
                        val title = "${getString(R.string.app_name)}客服"
                        QYHelper.openQYService(requireContext(), title)
                    }
                    9 -> {//设置
                        startActivity(Intent(context, SettingActivity::class.java))
                    }
                }
            }
        }
    }

    private fun loadMenuData() {
        val menuData: MutableList<MeMenuBean> = ArrayList()
        menuData.add(MeMenuBean(1, "机具管理", R.drawable.dy_ic_me_menu_transfer))
//        menuData.add(MeMenuBean(1, "机具划拨", R.drawable.dy_ic_me_menu_transfer))
//        menuData.add(MeMenuBean(2, "机具采购", R.drawable.dy_ic_me_menu_purchase))
//        menuData.add(MeMenuBean(3, "讲武堂", R.drawable.dy_ic_me_menu_school))
        menuData.add(MeMenuBean(4, "积分兑换", R.drawable.dy_ic_me_menu_exchange))
//        menuData.add(MeMenuBean(5, "银行卡", R.drawable.dy_ic_me_menu_bank_card))
        menuData.add(MeMenuBean(7, "授权书", R.drawable.dy_ic_me_menu_auth))
        menuData.add(MeMenuBean(8, "在线客服", R.drawable.ic_me_menu_online_service))
        menuData.add(MeMenuBean(6, "PK", R.drawable.dy_ic_me_menu_pk))
        menuData.add(MeMenuBean(9, "设置", R.drawable.dy_ic_me_menu_setting))
        mAdapter?.data = menuData
    }

    override fun onStart() {
        super.onStart()
        fetchAuthResult()
        fetchPersonalData()
//        fetchIntegralBalance()
    }

    private fun fetchPersonalData() {
        mPresenter?.fetchPersonalData()
    }

    override fun bindPersonalData(personalBean: PersonalBean?) {
        personalBean?.let {
            //头像
            iv_avatar.load(it.avatar) {
                crossfade(true)//淡入效果
                allowHardware(false)
                placeholder(R.drawable.dy_img_avatar_def)
                error(R.drawable.dy_img_avatar_def)
                transformations(CircleCropTransformation())
            }
            //昵称
            tv_name.text = if (TextUtils.isEmpty(it.name)) {
                it.username
            } else {
                it.name
            }
            //等级
            tv_level.text = it.title
            //账户总额
            tv_amount.text = NumberUtils.numberScale(it.total)
            //个人收益
            tv_amount_personal.text = NumberUtils.formatMoney(it.personal)
            //团队收益
            tv_amount_team.text = NumberUtils.formatMoney(it.team)
            //激活返现
            tv_amount_activation.text = NumberUtils.formatMoney(it.personalActive)
            //采购奖励
            tv_amount_purchase.text = NumberUtils.formatMoney(it.purchase)
            //团队名称
            tv_team_name.text = if (TextUtils.isEmpty(it.teamName)) {
                "--的团队"
            } else {
                it.teamName
            }

            tv_parent_name.text = it.parentName ?: ""
            tv_parent_phone.text = it.parentPhone ?: ""
        }
        cl_my_parent.visibility = if (TextUtils.isEmpty(personalBean?.parentPhone)) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun fetchAuthResult() {
        mPresenter?.fetchAuthResult()
    }

    override fun bindAuthResult(authResult: AuthResult) {
        when (authResult.status) {
            "0" -> {
                tv_realname_status?.text = "审核中"
            }
            "2" -> {
                tv_realname_status?.text = "已认证"
            }
            else -> {
                tv_realname_status?.text = "未认证"
            }
        }
    }

    private fun fetchIntegralBalance() {
        mPresenter?.fetchIntegralBalance("")
    }

    override fun bindIntegralBalance(data: List<IntegralBalanceBean>?) {
        if (data.isNullOrEmpty()) {
            tv_integral_left_title?.visibility = View.GONE
            tv_integral_right_title?.visibility = View.GONE
            tv_integral_left?.visibility = View.GONE
            tv_integral_right?.visibility = View.GONE
        } else {
            var leftBean: IntegralBalanceBean? = null
            var rightBean: IntegralBalanceBean? = null
            for (bean in data) {
                if (leftBean == null) {
                    leftBean = bean
                } else if (rightBean == null) {
                    rightBean = bean
                } else {
                    break
                }
            }
            tv_integral_left_title?.visibility = if (leftBean == null) {
                View.GONE
            } else {
                View.VISIBLE
            }
            tv_integral_right_title?.visibility = if (rightBean == null) {
                View.GONE
            } else {
                View.VISIBLE
            }
            tv_integral_left?.visibility = if (leftBean == null) {
                View.GONE
            } else {
                View.VISIBLE
            }
            tv_integral_right?.visibility = if (rightBean == null) {
                View.GONE
            } else {
                View.VISIBLE
            }
            //积分标题
            tv_integral_left_title?.text = if (leftBean == null) {
                "--"
            } else {
                leftBean?.name
            }
            tv_integral_right_title?.text = if (rightBean == null) {
                "--"
            } else {
                rightBean?.name
            }
            //tag
            tv_integral_left_title?.setTag(
                R.id.dy_tv_tag, if (leftBean == null) {
                    ""
                } else {
                    leftBean.machineTypeId
                }
            )
            tv_integral_right_title?.setTag(
                R.id.dy_tv_tag, if (rightBean == null) {
                    ""
                } else {
                    rightBean.machineTypeId
                }
            )

            //积分
            tv_integral_left?.text = if (leftBean == null) {
                "--"
            } else {
                leftBean?.point
            }
            tv_integral_right?.text = if (rightBean == null) {
                "--"
            } else {
                rightBean?.point
            }
        }
    }

}