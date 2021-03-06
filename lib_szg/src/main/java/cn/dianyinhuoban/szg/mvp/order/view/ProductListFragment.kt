package cn.dianyinhuoban.szg.mvp.order.view

import android.os.Bundle
import android.text.Html
import android.text.TextUtils
import android.view.View
import cn.dianyinhuoban.szg.R
import cn.dianyinhuoban.szg.mvp.bean.PurchaseProductBean
import cn.dianyinhuoban.szg.mvp.order.contract.ProductListContract
import cn.dianyinhuoban.szg.mvp.order.presenter.ProductListPresenter
import coil.load
import coil.transform.RoundedCornersTransformation
import com.wareroom.lib_base.ui.BaseListFragment
import com.wareroom.lib_base.ui.adapter.SimpleAdapter
import com.wareroom.lib_base.utils.DimensionUtils
import com.wareroom.lib_base.utils.NumberUtils
import kotlinx.android.synthetic.main.dy_item_product_list.view.*

class ProductListFragment : BaseListFragment<PurchaseProductBean, ProductListPresenter>(),
    ProductListContract.View {
    private var mTypeID: String? = null

    companion object {
        fun newInstance(typeID: String): ProductListFragment {
            val args = Bundle()
            args.putString("typeID", typeID)
            val fragment = ProductListFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mTypeID = arguments?.getString("typeID", "")
    }

    private val mDp2px6 by lazy {
        DimensionUtils.dp2px(requireContext(), 6).toFloat()
    }

    override fun getPresenter(): ProductListPresenter {
        return ProductListPresenter(this)
    }

    override fun onRequest(page: Int) {
        mPresenter.fetchPurchaseProduct(mTypeID ?: "", page)
    }

    override fun getItemLayout(): Int {
        return R.layout.dy_item_product_list
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onRequest(DEF_START_PAGE)
    }

    override fun convert(
        viewHolder: SimpleAdapter.SimpleViewHolder?,
        position: Int,
        itemData: PurchaseProductBean?
    ) {
        viewHolder?.itemView?.iv_cover?.load(itemData?.img) {
            crossfade(true)
        }
        val title = StringBuffer()
        if (!itemData?.act_cashback.isNullOrBlank()) {
            title.append("?????????&thinsp;<font color='red'> ${NumberUtils.formatMoney(itemData?.act_cashback)}???</font>")
        }
        if (!itemData?.back_point.isNullOrBlank()) {
            if (title.isNotBlank()) {
                title.append("&thinsp;+&thinsp;")
            }
            title.append("<font color='red'>${NumberUtils.formatMoney(itemData?.back_point)}</font>&thinsp;??????")
        }
        if (!itemData?.back_point.isNullOrBlank()) {
            if (title.isNotBlank()) {
                title.append("<br/>")
            }
            title.append("?????????&thinsp;<font color='red'>${NumberUtils.formatMoney(itemData?.standard_cashback)}</font>&thinsp;???")
        }
        viewHolder?.itemView?.tv_title?.text = Html.fromHtml(title.toString())
        viewHolder?.itemView?.tv_title_des?.text = if (itemData?.set_meal.isNullOrBlank()) {
            ""
        } else {
            "${itemData?.set_meal}??????"
        }
        viewHolder?.itemView?.tv_price?.text = if (TextUtils.isEmpty(itemData?.price)) {
            "--"
        } else {
            "??${NumberUtils.formatMoney(itemData?.price)}"
        }
        viewHolder?.itemView?.iv_buy?.setOnClickListener {
            itemData?.let {
                ProductDetailActivity.openProductDetailActivity(
                    requireContext(),
                    mTypeID ?: "",
                    itemData
                )
            }
        }
    }

    override fun onItemClick(data: PurchaseProductBean?, position: Int) {
//        data?.let {
//            ProductDetailActivity.openProductDetailActivity(requireContext(), data)
//        }
    }

    override fun bindPurchaseProduct(productData: List<PurchaseProductBean>?) {
        loadData(productData)
    }
}