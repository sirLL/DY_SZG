package cn.dianyinhuoban.szg.mvp.machine.view

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import cn.dianyinhuoban.szg.R
import cn.dianyinhuoban.szg.mvp.bean.MachineTypeBean
import cn.dianyinhuoban.szg.mvp.machine.contract.MachineTypeContract
import cn.dianyinhuoban.szg.mvp.machine.presenter.MachineTypePresenter
import com.dy.tablayout.SlidingTabLayout
import com.wareroom.lib_base.ui.BaseActivity

class MachineManagerActivity : BaseActivity<MachineTypeContract.Presenter?>(),
    MachineTypeContract.View {

    private var tabLayout: SlidingTabLayout? = null
    private var viewPager: ViewPager? = null
    private val fragmentList: ArrayList<Fragment> by lazy {
        arrayListOf()
    }

    override fun getPresenter(): MachineTypeContract.Presenter? {
        return MachineTypePresenter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dy_activity_machine_manager)
        setTitle("机具管理")
        setRightButtonText("划拨") {
            startActivity(Intent(this, MachineTransferActivity::class.java))
        }
        tabLayout = findViewById(R.id.tab_layout)
        viewPager = findViewById(R.id.view_pager)
        findViewById<TextView>(R.id.tv_filter)?.setOnClickListener {
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
            supportFragmentManager,
            fragmentList
        )
    }
}