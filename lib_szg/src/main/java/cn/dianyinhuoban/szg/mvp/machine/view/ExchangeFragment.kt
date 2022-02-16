package cn.dianyinhuoban.szg.mvp.machine.view

import android.os.Bundle
import android.text.Html
import android.text.TextUtils
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import cn.dianyinhuoban.szg.R
import cn.dianyinhuoban.szg.mvp.bean.IntegralBalanceBean
import cn.dianyinhuoban.szg.mvp.bean.PurchaseProductBean
import cn.dianyinhuoban.szg.mvp.income.view.IntegralRecordActivity
import cn.dianyinhuoban.szg.mvp.machine.contract.ExchangeContract
import cn.dianyinhuoban.szg.mvp.machine.presenter.ExchangePresenter
import cn.dianyinhuoban.szg.mvp.order.view.ProductDetailActivity
import coil.load
import com.wareroom.lib_base.ui.BaseMultiListFragment
import com.wareroom.lib_base.ui.adapter.MultiAdapter
import com.wareroom.lib_base.utils.NumberUtils
import kotlinx.android.synthetic.main.dy_item_product_list.view.*

class ExchangeFragment :
    BaseMultiListFragment<ExchangeFragment.ExchangeItemDataBean?, ExchangeContract.Presenter?>(),
    ExchangeContract.View {

    companion object {
        fun newInstance(): ExchangeFragment {
            val args = Bundle()
            val fragment = ExchangeFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private var tvLeftTitle: TextView? = null
    private var tvLeftIntegral: TextView? = null
    private var tvRightTitle: TextView? = null
    private var tvRightIntegral: TextView? = null
    private var rlLeftContainer: RelativeLayout? = null
    private var rlRightContainer: RelativeLayout? = null
    private var tvCenterTitle: TextView? = null
    private var tvCenterIntegral: TextView? = null
    private var rlCenterContainer: RelativeLayout? = null


    override fun getContentView(): Int {
        return R.layout.dy_fragment_exchange
    }

    override fun isSupportLoadMore(): Boolean {
        return false
    }

    override fun getItemLayout(viewType: Int): Int {
        return if (viewType == ExchangeItemDataBean.ITEM_TYPE_TITLE) {
            R.layout.dy_item_exchange_title
        } else {
            R.layout.dy_item_exchange_machine
        }
    }

    override fun initView(contentView: View?) {
        super.initView(contentView)
        tvLeftTitle = contentView?.findViewById(R.id.tv_title_left)
        tvLeftIntegral = contentView?.findViewById(R.id.tv_amount_left)
        tvCenterTitle = contentView?.findViewById(R.id.tv_title_center)
        tvCenterIntegral = contentView?.findViewById(R.id.tv_amount_center)
        tvRightTitle = contentView?.findViewById(R.id.tv_title_right)
        tvRightIntegral = contentView?.findViewById(R.id.tv_amount_right)
        rlLeftContainer = contentView?.findViewById(R.id.ll_left_container)
        rlRightContainer = contentView?.findViewById(R.id.ll_right_container)
        rlCenterContainer = contentView?.findViewById(R.id.ll_center_container)
        rlLeftContainer?.setOnClickListener {
            val machineType = it.getTag(R.id.dy_tv_tag) as String
            IntegralRecordActivity.open(requireContext(), machineType)
        }
        rlRightContainer?.setOnClickListener {
            val machineType = it.getTag(R.id.dy_tv_tag) as String
            IntegralRecordActivity.open(requireContext(), machineType)
        }
        rlCenterContainer?.setOnClickListener {
            val machineType = it.getTag(R.id.dy_tv_tag) as String
            IntegralRecordActivity.open(requireContext(), machineType)
        }
    }

    override fun getPresenter(): ExchangeContract.Presenter? {
        return ExchangePresenter(this)
    }

    override fun onRequest(page: Int) {
        if (isSupportRefresh) {
            mPresenter?.fetchIntegralBalance("")
        }
        mPresenter?.fetchPurchaseProduct("", page)
    }

    override fun bindIntegralBalance(data: List<IntegralBalanceBean>?) {
        if (data.isNullOrEmpty()) {
            tvLeftTitle?.text = "--"
            tvLeftIntegral?.text = "--"
            tvRightTitle?.text = "--"
            tvRightIntegral?.text = "--"
            rlLeftContainer?.setTag(R.id.dy_tv_tag, "")
            rlRightContainer?.setTag(R.id.dy_tv_tag, "")
        } else {
            var leftBean: IntegralBalanceBean? = null
            var rightBean: IntegralBalanceBean? = null
            var centerBean: IntegralBalanceBean? = null
            for (bean in data) {
                if (rightBean == null) {
                    rightBean = bean
                } else if (centerBean == null) {
                    centerBean = bean
                } else if (leftBean == null) {
                    leftBean = bean
                } else {
                    break
                }
            }
            tvLeftTitle?.text = leftBean?.name
            tvLeftIntegral?.text = NumberUtils.formatMoney(leftBean?.point)
            tvCenterTitle?.text = centerBean?.name
            tvCenterIntegral?.text = NumberUtils.formatMoney(centerBean?.point)
            tvRightTitle?.text = rightBean?.name
            tvRightIntegral?.text = NumberUtils.formatMoney(rightBean?.point)
            rlLeftContainer?.setTag(R.id.dy_tv_tag, leftBean?.machineTypeId ?: "")
            rlRightContainer?.setTag(R.id.dy_tv_tag, rightBean?.machineTypeId ?: "")
            rlCenterContainer?.setTag(R.id.dy_tv_tag, centerBean?.machineTypeId ?: "")
        }

    }

    override fun bindPurchaseProduct(productData: List<PurchaseProductBean>?) {
        val mapData = linkedMapOf<String, MutableList<PurchaseProductBean>>()
        if (!productData.isNullOrEmpty()) {
            for (product in productData) {
                if (!product.typeName.isNullOrBlank()) {
                    var productList = mapData[product.typeName]
                    if (productList.isNullOrEmpty()) {
                        productList = mutableListOf()
                        mapData[product.typeName!!] = productList
                    }
                    productList.add(product)
                }
            }
        }

        val data = mutableListOf<ExchangeItemDataBean?>()
        for (mapItem in mapData.entries) {
            data.add(
                ExchangeItemDataBean(
                    ExchangeItemDataBean.ITEM_TYPE_TITLE,
                    mapItem.key,
                    null
                )
            )
            val machineList = mapItem.value
            for (machine in machineList) {
                data.add(
                    ExchangeItemDataBean(
                        ExchangeItemDataBean.ITEM_TYPE_MACHINE,
                        "",
                        machine
                    )
                )
            }
        }
        loadData(data)
    }


    override fun convert(
        viewHolder: MultiAdapter.SimpleViewHolder?,
        position: Int,
        viewType: Int,
        itemData: ExchangeItemDataBean?
    ) {
        if (viewType == ExchangeItemDataBean.ITEM_TYPE_TITLE) {
            viewHolder?.setText(R.id.tv_title, itemData?.title ?: "--")
        } else {
            viewHolder?.itemView?.iv_cover?.load(itemData?.machine?.img) {
                crossfade(true)
            }
            val title = StringBuffer()
            if (!itemData?.machine?.act_cashback.isNullOrBlank()) {
                title.append("激活奖&thinsp;<font color='red'> ${NumberUtils.formatMoney(itemData?.machine?.act_cashback)}元</font>")
            }
            if (!itemData?.machine?.back_point.isNullOrBlank()) {
                if (title.isNotBlank()) {
                    title.append("&thinsp;+&thinsp;")
                }
                title.append("<font color='red'>${NumberUtils.formatMoney(itemData?.machine?.back_point)}</font>&thinsp;购机积分")
            }
            if (!itemData?.machine?.back_point.isNullOrBlank()) {
                if (title.isNotBlank()) {
                    title.append("<br/>")
                }
                title.append("达标奖&thinsp;<font color='red'>${NumberUtils.formatMoney(itemData?.machine?.standard_cashback)}</font>&thinsp;元")
            }
            viewHolder?.itemView?.tv_title?.text = Html.fromHtml(title.toString())
            viewHolder?.itemView?.tv_title_des?.text =
                if (itemData?.machine?.set_meal.isNullOrBlank()) {
                    ""
                } else {
                    "${itemData?.machine?.set_meal}台包"
                }
            viewHolder?.itemView?.tv_price?.text =
                if (TextUtils.isEmpty(itemData?.machine?.price)) {
                    "--"
                } else {
                    "¥${NumberUtils.formatMoney(itemData?.machine?.price)}"
                }
            viewHolder?.itemView?.iv_buy?.setOnClickListener {
                itemData?.machine?.let {
                    ProductDetailActivity.openProductDetailActivity(
                        requireContext(),
                        it.type_id ?: "",
                        it
                    )
                }
            }
        }
    }

    override fun getItemViewType(position: Int, itemData: ExchangeItemDataBean?): Int {
        return if (itemData == null) {
            ExchangeItemDataBean.ITEM_TYPE_MACHINE
        } else {
            itemData.itemType ?: ExchangeItemDataBean.ITEM_TYPE_MACHINE
        }
    }

    override fun onItemClick(data: ExchangeItemDataBean?, position: Int, viewType: Int) {

    }


    class ExchangeItemDataBean(
        itemType: Int,
        title: String?,
        machine: PurchaseProductBean?
    ) {
        companion object {
            const val ITEM_TYPE_MACHINE = 0
            const val ITEM_TYPE_TITLE = 1
        }

        var itemType: Int? = null
        var title: String? = null
        var machine: PurchaseProductBean? = null

        init {
            this.itemType = itemType
            this.title = title
            this.machine = machine
        }

    }

}