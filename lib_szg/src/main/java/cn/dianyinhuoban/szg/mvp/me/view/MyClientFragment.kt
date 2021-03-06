package cn.dianyinhuoban.szg.mvp.me.view

import android.os.Bundle
import android.view.View
import cn.dianyinhuoban.szg.R
import cn.dianyinhuoban.szg.mvp.bean.MyClientBean
import cn.dianyinhuoban.szg.mvp.me.contract.MyClientContract
import cn.dianyinhuoban.szg.mvp.me.presenter.MyClientPresenter
import com.wareroom.lib_base.ui.BaseListFragment
import com.wareroom.lib_base.ui.adapter.SimpleAdapter
import com.wareroom.lib_base.utils.DateTimeUtils
import com.wareroom.lib_base.utils.NumberUtils
import kotlinx.android.synthetic.main.dy_item_my_client.view.*

class MyClientFragment : BaseListFragment<MyClientBean, MyClientPresenter?>(),
    MyClientContract.View {

    companion object {
        fun newInstance(): MyClientFragment {
            val args = Bundle()
            val fragment = MyClientFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getPresenter(): MyClientPresenter? {
        return MyClientPresenter(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoadingView()
        onRequest(DEF_START_PAGE)
    }

    override fun onRequest(page: Int) {
        mPresenter?.fetchMyClientBean(page)
    }

    override fun getItemLayout(): Int {
        return R.layout.dy_item_my_client
    }

    override fun bindMyClient(data: List<MyClientBean>?) {
        loadData(data)
    }

    override fun convert(
        viewHolder: SimpleAdapter.SimpleViewHolder?,
        position: Int,
        itemData: MyClientBean?,
    ) {
        viewHolder?.itemView?.tv_name?.text = itemData?.client ?: "--"
        viewHolder?.itemView?.tv_no?.text = itemData?.pos_sn ?: "--"
        viewHolder?.itemView?.tv_date?.text =
            DateTimeUtils.getYYYYMMDDHHMMSS((itemData?.act_time?.toLong() ?: 0) * 1000)
        viewHolder?.itemView?.tv_amount?.text = NumberUtils.formatMoney(itemData?.total)
    }

    override fun onItemClick(data: MyClientBean?, position: Int) {

    }
}