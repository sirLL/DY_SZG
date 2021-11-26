package cn.dianyinhuoban.szg.mvp.login.contract

import cn.dianyinhuoban.szg.mvp.bean.AuthResult
import cn.dianyinhuoban.szg.mvp.bean.UserBean
import com.wareroom.lib_base.mvp.IView
import com.wareroom.lib_http.response.Response
import io.reactivex.Observable

interface LoginContract {
    interface Model {
        fun submitLogin(userName: String, password: String): Observable<Response<UserBean?>>

        fun fetchAuthResult(token: String): Observable<Response<AuthResult?>>
    }

    interface Presenter {
        fun login(userName: String, password: String)

        fun fetchAuthResult(token: String)
    }

    interface View : IView {
        fun onLoginSuccess()

        fun showAuthResult(authResult: AuthResult, token: String)
    }
}