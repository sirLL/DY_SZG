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

        //??????
        view_income_action.setOnClickListener {
            startActivity(Intent(context, IncomeActivity::class.java))
        }
        //??????
        tv_withdraw.setOnClickListener {
            startActivity(Intent(context, WithdrawActivity::class.java))
        }
        //????????????
        cl_my_client.setOnClickListener {
            startActivity(Intent(context, MyClientActivity::class.java))
        }
        //????????????
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
                    .setMessage("????????????${name}???????")
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
                    1 -> {//????????????
                        startActivity(Intent(context, MachineManagerActivity::class.java))
                    }
                    2 -> {//????????????
                        startActivity(Intent(context, ProductListActivity::class.java))
                    }
                    3 -> {//?????????
                        startActivity(Intent(context, JiangWuTangActivity::class.java))
                    }
                    4 -> {//????????????
                        startActivity(Intent(context, ExchangeActivity::class.java))
                    }
                    5 -> {//?????????
                        startActivity(Intent(context, BankManageActivity::class.java))
                    }
                    6 -> {//PK
                        startActivity(Intent(context, PkActivity::class.java))
                    }
                    7 -> {//?????????
                        startActivity(Intent(context, AuthActivity::class.java))
                    }
                    8 -> {//????????????
                        val title = "${getString(R.string.app_name)}??????"
                        QYHelper.openQYService(requireContext(), title)
                    }
                    9 -> {//??????
                        startActivity(Intent(context, SettingActivity::class.java))
                    }
                }
            }
        }
    }

    private fun loadMenuData() {
        val menuData: MutableList<MeMenuBean> = ArrayList()
        menuData.add(MeMenuBean(1, "????????????", R.drawable.dy_ic_me_menu_transfer))
//        menuData.add(MeMenuBean(1, "????????????", R.drawable.dy_ic_me_menu_transfer))
//        menuData.add(MeMenuBean(2, "????????????", R.drawable.dy_ic_me_menu_purchase))
//        menuData.add(MeMenuBean(3, "?????????", R.drawable.dy_ic_me_menu_school))
        menuData.add(MeMenuBean(4, "????????????", R.drawable.dy_ic_me_menu_exchange))
//        menuData.add(MeMenuBean(5, "?????????", R.drawable.dy_ic_me_menu_bank_card))
        menuData.add(MeMenuBean(7, "?????????", R.drawable.dy_ic_me_menu_auth))
        menuData.add(MeMenuBean(8, "????????????", R.drawable.ic_me_menu_online_service))
        menuData.add(MeMenuBean(6, "PK", R.drawable.dy_ic_me_menu_pk))
        menuData.add(MeMenuBean(9, "??????", R.drawable.dy_ic_me_menu_setting))
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
            //??????
            iv_avatar.load(it.avatar) {
                crossfade(true)//????????????
                allowHardware(false)
                placeholder(R.drawable.dy_img_avatar_def)
                error(R.drawable.dy_img_avatar_def)
                transformations(CircleCropTransformation())
            }
            //??????
            tv_name.text = if (TextUtils.isEmpty(it.name)) {
                it.username
            } else {
                it.name
            }
            //??????
            tv_level.text = it.title
            tv_level.visibility = if (it.title.isNullOrEmpty()) {
                View.GONE
            } else {
                View.VISIBLE
            }
            tv_alias.text = it.alias
            tv_alias.visibility = if (it.alias.isNullOrEmpty()) {
                View.GONE
            } else {
                View.VISIBLE
            }
            //????????????
            tv_amount.text = NumberUtils.numberScale(it.total)
            //????????????
            tv_amount_personal.text = NumberUtils.formatMoney(it.personal)
            //????????????
            tv_amount_team.text = NumberUtils.formatMoney(it.team)
            //????????????
            tv_amount_activation.text = NumberUtils.formatMoney(it.personalActive)
            //????????????
            tv_amount_purchase.text = NumberUtils.formatMoney(it.purchase)
            //????????????
            tv_team_name.text = if (TextUtils.isEmpty(it.teamName)) {
                "--?????????"
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
                tv_realname_status?.text = "?????????"
            }
            "2" -> {
                tv_realname_status?.text = "?????????"
            }
            else -> {
                tv_realname_status?.text = "?????????"
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
            //????????????
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

            //??????
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