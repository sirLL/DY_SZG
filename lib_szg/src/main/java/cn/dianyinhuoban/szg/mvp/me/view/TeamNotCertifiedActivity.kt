package cn.dianyinhuoban.szg.mvp.me.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import cn.dianyinhuoban.szg.R
import com.wareroom.lib_base.mvp.IPresenter
import com.wareroom.lib_base.ui.BaseActivity

class TeamNotCertifiedActivity : BaseActivity<IPresenter?>() {
    companion object {
        fun open(context: Context, status: String) {
            val intent = Intent(context, TeamNotCertifiedActivity::class.java)
            val bundle = Bundle()
            bundle.putString("status", status)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
    }

    private var mStatus = ""

    override fun handleIntent(bundle: Bundle?) {
        super.handleIntent(bundle)
        mStatus = bundle?.getString("status", "1") ?: "1"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dy_activity_team_not_certified)
        setTitle(
            if ("2" == mStatus) {
                "已实名"
            } else {
                "未实名"
            }
        )
        supportFragmentManager.beginTransaction().add(
            R.id.fl_container,
            TeamNotCertifiedFragment.newInstance(mStatus),
            "TeamNotCertifiedFragment"
        ).commit()
    }

    override fun getPresenter(): IPresenter? {
        return null
    }
}