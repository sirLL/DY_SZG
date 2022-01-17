package cn.dianyinhuoban.szg.mvp

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import cn.dianyinhuoban.szg.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tbruyelle.rxpermissions2.RxPermissions
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.ui.BaseActivity
import com.wareroom.lib_base.ui.adapter.SimpleAdapter
import com.wareroom.lib_base.utils.BitmapUtils
import com.wareroom.lib_base.utils.DimensionUtils
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*

class PreviewImageActivity : BaseActivity<IPresenter?>() {
    companion object {
        private const val TAG = "PreviewImageActivity"
        fun open(context: Context, imageData: Array<String>) {
            val intent = Intent(context, PreviewImageActivity::class.java)
            val bundle = Bundle()
            bundle.putStringArray("imageData", imageData)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
    }

    override fun toolbarIsEnable(): Boolean {
        return false
    }

    private var viewPager: ViewPager2? = null
    private var tvIndex: TextView? = null
    private val imageData by lazy {
        mutableListOf<String>()
    }

    override fun handleIntent(bundle: Bundle?) {
        super.handleIntent(bundle)
        val data = bundle?.getStringArray("imageData")
        data?.let {
            for (image in it) {
                if (!image.isNullOrBlank()) {
                    imageData.add(image)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dy_activity_preview_image)
        viewPager = findViewById(R.id.view_pager)
        tvIndex = findViewById(R.id.tv_index)
        findViewById<ImageView>(R.id.iv_cancel).setOnClickListener {
            finish()
        }

        viewPager?.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tvIndex?.text = "${position + 1}/${imageData.size}"
            }
        })

        val adapter = ImageAdapter()
        viewPager?.adapter = adapter
        adapter.data = imageData

    }

    override fun getPresenter(): IPresenter? {
        return null
    }

    inner class ImageAdapter : SimpleAdapter<String>(R.layout.dy_item_preview_image) {
        override fun onItemClick(data: String?, position: Int) {

        }

        override fun convert(viewHolder: SimpleViewHolder?, position: Int, itemData: String?) {
            val ivPreview = viewHolder?.getView<ImageView>(R.id.iv_preview)
            val screenWidth = DimensionUtils.getScreenWidth(this@PreviewImageActivity)
            Glide.with(this@PreviewImageActivity).asBitmap().load(itemData)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        resource?.let {
                            if (it.width > 0) {
                                val lp = ivPreview?.layoutParams
                                val height = it.height * 1.0 / it.width * screenWidth
                                lp?.width = screenWidth
                                lp?.height = height.toInt()
                                ivPreview?.layoutParams = lp
                                ivPreview?.setImageBitmap(resource)
                            }
                        }
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {

                    }

                })

            viewHolder?.getView<TextView>(R.id.tv_save)?.setOnClickListener {
                ivPreview?.let { iv ->
                    saveView(iv)
                }

            }
        }

    }

    private fun saveView(view: View) {
        RxPermissions(PreviewImageActivity@ this).request(
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).subscribe { aBoolean: Boolean ->
            if (aBoolean) {
                saveView2File(view)
            } else {
                showToast("你尚未开启读写权限")
            }
        }
    }

    private fun saveView2File(view: View) {
        showLoading()
        Observable.just(view)
            .map { v ->
                val fileName = "DYHM${Calendar.getInstance().timeInMillis}.jpg"
                BitmapUtils.saveBitmap(
                    PreviewImageActivity@ this,
                    BitmapUtils.view2Bitmap(v),
                    fileName
                )
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<String> {
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(path: String) {
                    hideLoading()
                    showToast("图片保存至${path}")
                }

                override fun onError(e: Throwable) {
                    hideLoading()
                    showToast("图片保存失败")
                }

                override fun onComplete() {}
            })
    }
}