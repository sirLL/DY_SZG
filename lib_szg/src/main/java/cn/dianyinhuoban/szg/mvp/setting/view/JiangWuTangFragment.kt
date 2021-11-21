package cn.dianyinhuoban.szg.mvp.setting.view

import android.os.Bundle
import android.view.View
import cn.dianyinhuoban.szg.R
import cn.dianyinhuoban.szg.mvp.WebActivity
import cn.dianyinhuoban.szg.mvp.bean.JiangWuTangBean
import cn.dianyinhuoban.szg.mvp.setting.contract.SettingContract
import cn.dianyinhuoban.szg.mvp.setting.presenter.SettingPresenter
import com.wareroom.lib_base.ui.BaseListFragment
import com.wareroom.lib_base.ui.adapter.SimpleAdapter
import kotlinx.android.synthetic.main.dy_item_jwt.view.*

class JiangWuTangFragment : BaseListFragment<JiangWuTangBean, SettingPresenter>(),
    SettingContract.View {

    override fun getPresenter(): SettingPresenter {
        return SettingPresenter(this)
    }

    override fun onRequest(page: Int) {
        mPresenter.getJiangWuTangList(page)
    }

    override fun getItemLayout(): Int {
        return R.layout.dy_item_jwt
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onRequest(DEF_START_PAGE)
    }

    override fun convert(
        viewHolder: SimpleAdapter.SimpleViewHolder?,
        position: Int,
        itemData: JiangWuTangBean?
    ) {
        viewHolder?.itemView?.tv_title?.text = itemData?.title

    }

    override fun onItemClick(data: JiangWuTangBean?, position: Int) {
        data?.let {
            WebActivity.openWebActivity(requireContext(), it.title, it.url)
        }
    }

    override fun onLoadJWTList(data: MutableList<JiangWuTangBean>) {
        super.onLoadJWTList(data)
        loadData(data)
    }

}