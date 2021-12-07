package cn.dianyinhuoban.szg.mvp.home.view

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import cn.dianyinhuoban.szg.DYHelper
import cn.dianyinhuoban.szg.R
import cn.dianyinhuoban.szg.mvp.auth.view.RealnameAuthActivity
import cn.dianyinhuoban.szg.mvp.bean.AuthResult
import cn.dianyinhuoban.szg.mvp.bean.SystemItemBean
import cn.dianyinhuoban.szg.mvp.home.contract.SystemContract
import cn.dianyinhuoban.szg.mvp.home.presenter.SystemPresenter
import cn.dianyinhuoban.szg.mvp.me.view.MeFragment
import cn.dianyinhuoban.szg.mvp.order.view.ProductListParentFragment
import cn.dianyinhuoban.szg.mvp.poster.view.PosterActivity
import cn.dianyinhuoban.szg.mvp.poster.view.PosterFragment
import cn.dianyinhuoban.szg.mvp.ranking.view.RankingFragment
import cn.dianyinhuoban.szg.qiyu.QYHelper
import cn.dianyinhuoban.szg.widget.dialog.MessageDialog
import com.qiyukf.unicorn.api.Unicorn
import com.tencent.mmkv.MMKV
import com.tencent.smtt.sdk.QbSdk
import com.wareroom.lib_base.ui.BaseActivity
import com.wareroom.lib_base.utils.AppManager
import com.wareroom.versionchecklib.core.http.HttpHeaders
import com.wareroom.versionchecklib.core.http.HttpParams
import com.wareroom.versionchecklib.core.http.HttpRequestMethod
import com.wareroom.versionchecklib.v2.AllenVersionChecker
import com.wareroom.versionchecklib.v2.builder.DownloadBuilder
import com.wareroom.versionchecklib.v2.builder.UIData
import com.wareroom.versionchecklib.v2.callback.CustomDownloadingDialogListener
import com.wareroom.versionchecklib.v2.callback.CustomVersionDialogListener
import com.wareroom.versionchecklib.v2.callback.RequestVersionListener
import kotlinx.android.synthetic.main.dy_activity_home.*
import java.util.*

class HomeActivity : BaseActivity<SystemPresenter?>(), SystemContract.View {
    var mAuthResult: AuthResult? = null
    override fun getPresenter(): SystemPresenter? {
        return SystemPresenter(this)
    }

    override fun toolbarIsEnable(): Boolean {
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initX5WebView()
        Unicorn.initSdk()
        setContentView(R.layout.dy_activity_home)
        initView()
    }

    override fun onStart() {
        super.onStart()
        mPresenter?.fetchSystemSetting()
        mPresenter?.fetchAuthResult()
        fetchVersion()
    }

    private fun initView() {
        setupNavigationBar()
        initFragment()
        iv_custom_service.setOnClickListener {
            val title = "${getString(R.string.app_name)}客服"
            QYHelper.openQYService(HomeActivity@ this, title)
        }
        cover_view.setOnClickListener {
            showAuthDialog()
        }
    }

    /**
     * 底部导航栏
     */
    private fun setupNavigationBar() {
        navigation_bar.addTab(
            R.drawable.dy_ic_nav_home_normal, R.drawable.dy_ic_nav_home_selector,
            ContextCompat.getColor(HomeActivity@ this, R.color.color_nav_normal),
            ContextCompat.getColor(HomeActivity@ this, R.color.color_nav_selector), "首页"
        )
        navigation_bar.addTab(
            R.drawable.dy_ic_nav_machine_normal, R.drawable.dy_ic_nav_machine_selector,
            ContextCompat.getColor(HomeActivity@ this, R.color.color_nav_normal),
            ContextCompat.getColor(HomeActivity@ this, R.color.color_nav_selector), "掌柜商城"
        )
        navigation_bar.addTab(
            R.drawable.dy_ic_nav_poster_normal, R.drawable.dy_ic_nav_poster_selector,
            ContextCompat.getColor(HomeActivity@ this, R.color.color_nav_normal),
            ContextCompat.getColor(HomeActivity@ this, R.color.color_nav_selector), "讲武堂"
        )
        navigation_bar.addTab(
            R.drawable.dy_ic_nav_leaderboard_normal, R.drawable.dy_ic_nav_leaderboard_selector,
            ContextCompat.getColor(HomeActivity@ this, R.color.color_nav_normal),
            ContextCompat.getColor(HomeActivity@ this, R.color.color_nav_selector), "排行榜"
        )
        navigation_bar.addTab(
            R.drawable.dy_ic_nav_me_normal, R.drawable.dy_ic_nav_me_selector,
            ContextCompat.getColor(HomeActivity@ this, R.color.color_nav_normal),
            ContextCompat.getColor(HomeActivity@ this, R.color.color_nav_selector), "我的"
        )

        navigation_bar.setOnItemTabClickCallBack { index, _ ->
            if (index == 2) {
                startActivity(Intent(HomeActivity@ this, PosterActivity::class.java))
                true
            } else {
                false
            }
        }
    }

    private fun initFragment() {
        val fragmentList: MutableList<Fragment> = mutableListOf()
        fragmentList.add(HomeFragment.newInstance())
        fragmentList.add(ProductListParentFragment.newInstance())
        fragmentList.add(PosterFragment.newInstance())
        fragmentList.add(RankingFragment.newInstance())
        fragmentList.add(MeFragment.newInstance())
        navigation_bar.setupFragments(supportFragmentManager, R.id.fl_container, fragmentList)
        navigation_bar.checkedTab = 0
    }

    override fun bindSystemBean(systemData: List<SystemItemBean>?) {
        systemData?.let { it ->
            for (systemItemBean in it) {
                systemItemBean?.id?.let {
                    when (it) {
                        "1" -> {
                            //安卓版本号
                        }
                        "2" -> {
                            //IOS版本号
                        }
                        "3" -> {
                            //安卓下载链接
                        }
                        "4" -> {
                            //IOS下载链接
                        }
                        "5" -> {
                            //总部电话
                            MMKV.defaultMMKV().encode("COMPANY_PHONE", systemItemBean.content)
                        }
                        else -> {

                        }
                    }
                }
            }
        }
    }

    override fun bindAuthResult(authResult: AuthResult) {
        mAuthResult = authResult
        when (authResult.status) {
            "2" -> {
                cover_view.visibility = View.GONE
            }
            else -> {
                cover_view.visibility = View.VISIBLE
                showAuthDialog()
            }
        }
    }

    private fun showAuthDialog() {
        mAuthResult?.let { authResult ->
            val message = if ("0" == authResult.status) {
                "实名认证正在审核中，请稍后再试"
            } else {
                "您尚未完成实名认证，去认证?"
            }
            val messageDialog = MessageDialog(this)
                .setMessage(message)
                .setOnConfirmClickListener {
                    if ("0" == authResult.status) {
                        it.dismiss()
                    } else {
                        it.dismiss()
                        startActivity(Intent(HomeActivity@ this, RealnameAuthActivity::class.java))
                    }
                }
                .setOnCancelClickListener {
                    AppManager.getInstance().loginOut(HomeActivity@ this)
                }
            messageDialog.setCanceledOnTouchOutside(false)
            messageDialog.show()
        }
    }

    private fun initX5WebView() {
        //x5内核初始化接口
        QbSdk.initX5Environment(applicationContext, object : QbSdk.PreInitCallback {
            override fun onCoreInitFinished() {
                Log.d("QbSdk", "onCoreInitFinished: ")
            }

            override fun onViewInitFinished(p0: Boolean) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("QbSdk", "onViewInitFinished: $p0")
            }

        })
    }

    private fun fetchVersion() {
        if (DYHelper.getInstance().onCheckVersionCallback == null
            || DYHelper.getInstance().onCheckVersionCallback.requestUrl.isNullOrBlank()) return
        val checker = AllenVersionChecker.getInstance().requestVersion()
        val headerMap = DYHelper.getInstance().onCheckVersionCallback?.fetchVersionHeader
        val paramsMap = DYHelper.getInstance().onCheckVersionCallback?.fetchVersionParams
        if (!headerMap.isNullOrEmpty()) {
            val headers = HttpHeaders()
            for (entry in headerMap.entries) {
                headers[entry.key] = entry.value
            }
            checker.httpHeaders = headers
        }
        if (!paramsMap.isNullOrEmpty()) {
            val params = HttpParams()
            for (entry in paramsMap.entries) {
                params[entry.key] = entry.value
            }
            checker.requestParams = params
        }
        val requestMethod = DYHelper.getInstance().onCheckVersionCallback.requestMethod
        checker.requestMethod = when (requestMethod) {
            DYHelper.RequestMethod.POSTJSON -> {
                HttpRequestMethod.POSTJSON
            }
            DYHelper.RequestMethod.POST -> {
                HttpRequestMethod.POST
            }
            else -> {
                HttpRequestMethod.GET
            }
        }
        checker.requestUrl = DYHelper.getInstance().onCheckVersionCallback.requestUrl
        checker.request(object : RequestVersionListener {
            override fun onRequestVersionSuccess(
                downloadBuilder: DownloadBuilder?,
                result: String?
            ): UIData? {
                var uiDate: UIData? = null
                val downloadUrl =
                    DYHelper.getInstance().onCheckVersionCallback.onCheckVersion(result)
                if (!downloadUrl.isNullOrBlank()) {
                    uiDate = UIData.create().setDownloadUrl(downloadUrl)
                        .setTitle("版本更新")
                        .setContent("发现新版本，为了您更好的体验，需要更新APP")
                }
                downloadBuilder?.setForceUpdateListener {
                    AppManager.getInstance().exitApp()
                }
                downloadBuilder?.isRunOnForegroundService = true
                return uiDate
            }

            override fun onRequestVersionFailure(message: String?) {

            }

        }).setForceUpdateListener {
            AppManager.getInstance().exitApp()
        }.setCustomVersionDialogListener(createVersionDialog())
            .setCustomDownloadingDialogListener(createDownloadingDialog())
            .executeMission(HomeActivity@ this);
    }

    private fun createVersionDialog(): CustomVersionDialogListener? {
        return CustomVersionDialogListener { context: Context, versionBundle: UIData ->
            val versionDialog = Dialog(context, R.style.MessageDialog)
            versionDialog.setContentView(R.layout.dy_dialog_version_check)
            val textView: TextView = versionDialog.findViewById(R.id.tv_msg)
            textView.text = versionBundle.content
            versionDialog.setCanceledOnTouchOutside(false)
            versionDialog
        }
    }

    private fun createDownloadingDialog(): CustomDownloadingDialogListener? {
        return object : CustomDownloadingDialogListener {
            override fun getCustomDownloadingDialog(
                context: Context,
                progress: Int,
                versionBundle: UIData,
            ): Dialog {
                val downloadDialog = Dialog(context!!, R.style.MessageDialog)
                downloadDialog.setContentView(R.layout.dy_dialog_version_download)
                return downloadDialog
            }

            override fun updateUI(dialog: Dialog, progress: Int, versionBundle: UIData) {
                val tvProgress = dialog.findViewById<TextView>(R.id.tv_progress)
                val progressBar = dialog.findViewById<ProgressBar>(R.id.pb)
                progressBar.progress = progress
                tvProgress.text = getString(R.string.versionchecklib_progress, progress)
            }
        }
    }

    override fun onDestroy() {
        AllenVersionChecker.getInstance().cancelAllMission()
        super.onDestroy()
    }
}