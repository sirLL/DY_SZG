package cn.dianyinhuoban.szg.mvp.gift.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder
import cn.dianyinhuoban.szg.R
import cn.dianyinhuoban.szg.api.URLConfig
import cn.dianyinhuoban.szg.bean.GiftInfoBean
import cn.dianyinhuoban.szg.mvp.gift.contract.GiftContract
import cn.dianyinhuoban.szg.mvp.gift.presenter.GiftPresenter
import cn.dianyinhuoban.szg.widget.dialog.GiftDialog
import com.gyf.immersionbar.ImmersionBar
import com.tbruyelle.rxpermissions2.RxPermissions
import com.wareroom.lib_base.ui.BaseActivity
import com.wareroom.lib_base.utils.BitmapUtils
import com.wareroom.lib_base.utils.cache.MMKVUtil
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.util.*

class GiftActivity : BaseActivity<GiftContract.Presenter?>(), GiftContract.View {

    companion object {
        private const val ACTION_SAVE_IMG = 10
        private const val ACTION_SHARE_IMG = 11
    }

    private var tvName: TextView? = null
    private var tvContent: TextView? = null
    private var tvSignature: TextView? = null
    private var ivQR: ImageView? = null
    private var contentContainer: ConstraintLayout? = null
    private var giftDialog: GiftDialog? = null
    override fun getRootView(): Int {
        return R.layout.dy_activity_gift_root
    }

    override fun getBackButtonIcon(): Int {
        return R.drawable.dy_ic_back_circle
    }

    override fun getToolbarColor(): Int {
        return Color.TRANSPARENT
    }


    override fun initStatusBar() {
        ImmersionBar.with(this)
            .transparentStatusBar()
            .autoDarkModeEnable(isDarkModeEnable)
            .autoStatusBarDarkModeEnable(isDarkModeEnable)
            .statusBarView(findViewById(R.id.status_bar))
            .statusBarDarkFont(true)
            .flymeOSStatusBarFontColor(R.color.black)
            .init()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dy_activity_gift)
        tvName = findViewById(R.id.tv_name)
        tvContent = findViewById(R.id.tv_content)
        tvSignature = findViewById(R.id.tv_signature)
        contentContainer = findViewById(R.id.content_container)
        ivQR = findViewById(R.id.iv_qr)
        mPresenter?.fetchGiftInfo()

        val qrContent = URLConfig.PAGE_WEB_REGISTER + MMKVUtil.getInviteCode()
        createQR(qrContent)

        findViewById<ImageView>(R.id.tv_save).setOnClickListener {
            contentContainer?.let { container ->
                saveView(container, ACTION_SAVE_IMG)
            }

        }
        findViewById<ImageView>(R.id.tv_share).setOnClickListener {
            contentContainer?.let { container ->
                saveView(container, ACTION_SHARE_IMG)
            }
        }
    }

    override fun getPresenter(): GiftContract.Presenter? {
        return GiftPresenter(this)
    }

    override fun bindGiftInfo(data: GiftInfoBean) {
        tvName?.text = data.name
        tvContent?.text = data.content
        tvSignature?.text = data.signature
        showGiftDialog(data.money ?: "")
    }

    override fun onGetGiftSuccess() {
        giftDialog?.showAmount()
    }

    private fun showGiftDialog(amount: String) {
        if (giftDialog == null) {
            giftDialog = GiftDialog(this).setOnViewClickListener {
                mPresenter?.submitGetGift()
            }
        }
        giftDialog?.setAmount(amount)
        giftDialog?.show()
    }

    private fun createQR(qrContent: String) {
        Observable.just(qrContent)
            .map { s: String? ->
                val bitmap: Bitmap = BitmapFactory.decodeResource(
                    resources,
                    R.drawable.dy_ic_app_logo
                )
                QRCodeEncoder.syncEncodeQRCode(
                    s,
                    400,
                    Color.parseColor("#000000"),
                    bitmap
                )
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Bitmap> {
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(bitmap: Bitmap) {
                    ivQR?.setImageBitmap(bitmap)
                }

                override fun onError(e: Throwable) {
                    showToast("生成二维码失败")
                }

                override fun onComplete() {}
            })
    }

    private fun saveView(view: View, action: Int) {
        RxPermissions(ShareQRActivity@ this).request(
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).subscribe { aBoolean: Boolean ->
            if (aBoolean) {
                saveView2File(view, action)
            } else {
                showToast("你尚未开启读写权限")
            }
        }
    }

    private fun saveView2File(view: View, action: Int) {
        showLoading()
        Observable.just(view)
            .map { v ->
                val fileName = "DYHM${Calendar.getInstance().timeInMillis}.jpg"
                BitmapUtils.saveBitmap(
                    ShareQRActivity@ this,
                    BitmapUtils.view2Bitmap(v),
                    fileName
                )
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<String> {
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(path: String) {
                    dismissProgress()
                    if (action == ACTION_SAVE_IMG) {
                        showToast("图片保存至${path}")
                    } else {
                        shareFile(this@GiftActivity, path)
                    }
                }

                override fun onError(e: Throwable) {
                    showToast("图片保存失败")
                    dismissProgress()
                }

                override fun onComplete() {}
            })
    }

    private fun shareFile(context: Context, filePath: String) {
        val file = File(filePath)
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        val uri = Uri.fromFile(file)
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
        shareIntent.putExtra(Intent.EXTRA_TEXT, "分享内容")
        shareIntent.type = "image/*"
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK;
        context.startActivity(Intent.createChooser(shareIntent, getString(R.string.app_name)));
    }
}