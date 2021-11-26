package cn.dianyinhuoban.szg.mvp.order.view

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import cn.dianyinhuoban.szg.R
import cn.dianyinhuoban.szg.mvp.bean.MachineTypeBean
import cn.dianyinhuoban.szg.mvp.machine.contract.MachineTypeContract
import cn.dianyinhuoban.szg.mvp.machine.presenter.MachineTypePresenter
import cn.dianyinhuoban.szg.mvp.order.OrderListActivity
import com.dy.tablayout.SlidingTabLayout
import com.wareroom.lib_base.ui.BaseFragment

class ProductListParentFragment : BaseFragment<MachineTypePresenter?>(), MachineTypeContract.View {
    companion object {
        fun newInstance(): ProductListParentFragment {
            val args = Bundle()
            val fragment = ProductListParentFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private var viewPager: ViewPager? = null
    private var tabLayout: SlidingTabLayout? = null
    override fun getPresenter(): MachineTypePresenter? {
        return MachineTypePresenter(this)
    }

    override fun getContentView(): Int {
        return R.layout.dy_fragment_product_list_parent
    }

    override fun initView(contentView: View?) {
        super.initView(contentView)
        viewPager = contentView?.findViewById(R.id.view_pager)
        tabLayout = contentView?.findViewById(R.id.tab_layout)
        contentView?.findViewById<TextView>(R.id.tv_order)?.setOnClickListener {
            OrderListActivity.open(requireContext())
        }
        mPresenter?.fetchMachineType()
    }


    override fun bindMachineType(data: List<MachineTypeBean>?) {
        val titles: ArrayList<String> = arrayListOf<String>()
        val fragmentList = arrayListOf<Fragment>()
        data?.let {
            if (it.isNotEmpty()) {
                for (bean in it) {
                    bean?.let {
                        if (!TextUtils.isEmpty(bean.name) && !TextUtils.isEmpty(bean.id)) {
                            titles.add(bean.name)
                            fragmentList.add(ProductListFragment.newInstance(bean.id))
                        }
                    }
                }
            }
        }
        if (titles.size > 0) {
            val titleArr = arrayOfNulls<String>(titles.size)
            for (i in 0 until titles.size) {
                titleArr[i] = titles[i]
            }
            tabLayout?.setViewPager(
                viewPager,
                titleArr, childFragmentManager, fragmentList
            )
        }

    }
}