package cn.dianyinhuoban.szg.mvp.setting.view

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import cn.dianyinhuoban.szg.R
import cn.dianyinhuoban.szg.mvp.WebActivity
import cn.dianyinhuoban.szg.mvp.bean.JiangWuTangBean
import cn.dianyinhuoban.szg.mvp.setting.contract.SettingContract
import cn.dianyinhuoban.szg.mvp.setting.presenter.SettingPresenter
import coil.load
import com.wareroom.lib_base.ui.BaseListFragment
import com.wareroom.lib_base.ui.adapter.SimpleAdapter
import com.wareroom.lib_base.utils.DateTimeUtils
import kotlinx.android.synthetic.main.dy_item_jwt.view.*

class JiangWuTangFragment : BaseListFragment<JiangWuTangBean, SettingPresenter>(),
    SettingContract.View {

    companion object {
        fun newInstance(): JiangWuTangFragment {
            val args = Bundle()
            val fragment = JiangWuTangFragment()
            fragment.arguments = args
            return fragment
        }
    }

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
        viewHolder?.getView<ImageView>(R.id.iv_cover)?.load(itemData?.thumb) {
            crossfade(true)
        }
        viewHolder?.setText(R.id.tv_title, itemData?.title ?: "")
        viewHolder?.setText(
            R.id.tv_date, if (itemData?.updatetime.isNullOrBlank()) {
                ""
            } else {
                DateTimeUtils.formatDate(
                    itemData!!.updatetime.toLong() * 1000,
                    DateTimeUtils.PATTERN_YYYY_MM_DD
                )
            }
        )
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