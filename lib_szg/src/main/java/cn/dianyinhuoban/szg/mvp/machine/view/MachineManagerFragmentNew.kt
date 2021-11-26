package cn.dianyinhuoban.szg.mvp.machine.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import cn.dianyinhuoban.szg.R
import cn.dianyinhuoban.szg.mvp.bean.MachineTypeBean
import cn.dianyinhuoban.szg.mvp.machine.contract.MachineTypeContract
import cn.dianyinhuoban.szg.mvp.machine.presenter.MachineTypePresenter
import com.dy.tablayout.SlidingTabLayout
import com.wareroom.lib_base.ui.BaseFragment

class MachineManagerFragmentNew : BaseFragment<MachineTypeContract.Presenter?>(),
    MachineTypeContract.View {

    companion object {
        fun newInstance(): MachineManagerFragmentNew {
            val args = Bundle()
            val fragment = MachineManagerFragmentNew()
            fragment.arguments = args
            return fragment
        }
    }

    private var tabLayout: SlidingTabLayout? = null
    private var viewPager: ViewPager? = null
    private val fragmentList: ArrayList<Fragment> by lazy {
        arrayListOf()
    }

    override fun getContentView(): Int {
        return R.layout.dy_fragment_machine_manager_new
    }

    override fun getPresenter(): MachineTypeContract.Presenter? {
        return MachineTypePresenter(this)
    }

    override fun initView(contentView: View?) {
        super.initView(contentView)
        tabLayout = contentView?.findViewById(R.id.tab_layout)
        viewPager = contentView?.findViewById(R.id.view_pager)
        contentView?.findViewById<TextView>(R.id.tv_transfer)?.setOnClickListener {
            startActivity(Intent(context, MachineTransferActivity::class.java))
        }
        contentView?.findViewById<TextView>(R.id.tv_filter)?.setOnClickListener {
            val currentTab = tabLayout?.currentTab
            currentTab?.let {
                if (fragmentList.size > it) {
                    val fragment = fragmentList[currentTab]
                    fragment?.let { fm ->
                        (fm as MachineManagerChildFragment).showFilterDialog()
                    }
                }
            }
        }
        mPresenter?.fetchMachineType()
    }

    override fun bindMachineType(data: List<MachineTypeBean>?) {
        val titles = arrayListOf<String>()
        if (fragmentList.isNotEmpty()) {
            fragmentList.clear()
        }
        titles.add("全部")
        fragmentList.add(MachineManagerChildFragment.newInstance(""))

        data?.let {
            if (it.isNotEmpty()) {
                for (bean in it) {
                    if (!bean.name.isNullOrBlank()) {
                        titles.add(bean.name)
                        fragmentList.add(MachineManagerChildFragment.newInstance(bean.id ?: ""))
                    }
                }
            }
        }
        tabLayout?.setViewPager(
            viewPager,
            titles.toTypedArray(),
            childFragmentManager,
            fragmentList
        )
    }

}