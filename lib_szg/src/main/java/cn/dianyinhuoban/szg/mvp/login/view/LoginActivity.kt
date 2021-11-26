package cn.dianyinhuoban.szg.mvp.login.view

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import cn.dianyinhuoban.szg.DYHelper
import cn.dianyinhuoban.szg.R
import cn.dianyinhuoban.szg.event.CloseLoadingEvent
import cn.dianyinhuoban.szg.event.CloseLoginPageEvent
import cn.dianyinhuoban.szg.mvp.auth.view.RealnameAuthActivity
import cn.dianyinhuoban.szg.mvp.bean.AuthResult
import cn.dianyinhuoban.szg.mvp.home.view.HomeActivity
import cn.dianyinhuoban.szg.mvp.login.contract.LoginContract
import cn.dianyinhuoban.szg.mvp.login.presenter.LoginPresenter
import cn.dianyinhuoban.szg.widget.dialog.MessageDialog
import com.wareroom.lib_base.ui.BaseActivity
import com.wareroom.lib_base.utils.AppManager
import com.wareroom.lib_base.utils.ValidatorUtils
import com.wareroom.lib_base.utils.cache.MMKVUtil
import kotlinx.android.synthetic.main.dy_activity_login.*
import kotlinx.android.synthetic.main.dy_activity_setting.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class LoginActivity : BaseActivity<LoginPresenter?>(), LoginContract.View {
    private var showBackBtn = false
    override fun getPresenter(): LoginPresenter? {
        return LoginPresenter(this)
    }

    override fun toolbarIsEnable(): Boolean {
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
        setContentView(R.layout.dy_activity_login)
        initView()
        val defPhone = MMKVUtil.getUserName()
        val defPassword = MMKVUtil.getLoginPassword()
        ed_phone.setText(defPhone)
        ed_password.setText(defPassword)
        ed_phone.setSelection(defPhone.length)
        ed_password.setSelection(defPassword.length)
    }

    override fun handleIntent(bundle: Bundle?) {
        super.handleIntent(bundle)
        bundle?.let {
            showBackBtn = it.getBoolean("showBackBtn", false)
        }
    }

    private fun initView() {
        val textWatcher: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                setSubmitButtonEnable()
            }
        }
        iv_back.visibility = if (showBackBtn) View.VISIBLE else View.GONE
        iv_back.setOnClickListener {
            onBackPressed()
        }
        ed_phone.addTextChangedListener(textWatcher)
        ed_password.addTextChangedListener(textWatcher)
        iv_eye.setOnClickListener {
            it.isSelected = !it.isSelected
            if (it.isSelected) {
                //显示密码
                ed_password.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                //密码显示点点
                ed_password.transformationMethod = PasswordTransformationMethod.getInstance()
            }
            val password = ed_password.text.toString()
            ed_password.setSelection(password.length)
        }

        //注册
        tv_register.setOnClickListener {
            startActivity(Intent(LoginActivity@ this, RegisterActivity::class.java))
        }
        //忘记密码
        tv_forget.setOnClickListener {
            startActivity(Intent(LoginActivity@ this, ResetPasswordActivity::class.java))
        }
        //登录
        btn_submit.setOnClickListener {
            submitLogin()
        }
    }

    /**
     * 设置登录按钮是否可点击
     */
    private fun setSubmitButtonEnable() {
        val phone = ed_phone.text.toString()
        val password = ed_password.text.toString()
        btn_submit.isEnabled =
            (ValidatorUtils.isPassword(password) && ValidatorUtils.isMobile(phone))
    }

    /**
     * 登录
     */
    private fun submitLogin() {
        val userName = ed_phone.text.toString()
        val password = ed_password.text.toString()
        mPresenter?.login(userName, password)
    }

    override fun onLoginSuccess() {
        val intent = Intent(DYHelper.ACTION_LOGIN_SUCCESS)
        sendBroadcast(intent)
        startActivity(Intent(LoginActivity@ this, HomeActivity::class.java))
        finish()
    }

    override fun showAuthResult(authResult: AuthResult, token: String) {
        when (authResult.status) {
            "2", "0" -> {
                onLoginSuccess()
            }
            else -> {
                val message = "您尚未完成实名认证，去认证?"
                val messageDialog = MessageDialog(this)
                    .setMessage(message)
                    .setOnConfirmClickListener {
                        it.dismiss()
                        val intent = Intent(DYHelper.ACTION_LOGIN_SUCCESS)
                        sendBroadcast(intent)

                        val homeIntent = Intent(LoginActivity@ this, HomeActivity::class.java)
                        val realNameIntent =
                            Intent(LoginActivity@ this, RealnameAuthActivity::class.java)
                        startActivities(arrayOf(homeIntent, realNameIntent))
                        finish()
                    }
                    .setOnCancelClickListener {
                        onLoginSuccess()
                    }
                messageDialog.setCanceledOnTouchOutside(false)
                messageDialog.show()
            }
        }


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun onCloseLoading(event: CloseLoadingEvent) {
        hideLoading()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun onCloseLoginPage(event: CloseLoginPageEvent) {
        finish()
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

}