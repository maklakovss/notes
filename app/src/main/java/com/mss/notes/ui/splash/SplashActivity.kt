package com.mss.notes.ui.splash

import android.os.Handler
import com.mss.notes.ui.base.BaseActivity
import com.mss.notes.ui.main.MainActivity
import org.koin.android.viewmodel.ext.android.viewModel

private const val START_DELAY = 1000L

class SplashActivity : BaseActivity<Boolean?, SplashViewState>() {

    override val model: SplashViewModel by viewModel()

    override val layoutRes: Int? = null

    override fun onResume() {
        super.onResume()
        Handler().postDelayed({ model.requestUser() }, START_DELAY)
    }

    override fun renderData(data: Boolean?) {
        data?.takeIf { it }?.let {
            startMainActivity()
        }
    }

    private fun startMainActivity() {
        startActivity(MainActivity.getStartIntent(this))
        finish()
    }

}