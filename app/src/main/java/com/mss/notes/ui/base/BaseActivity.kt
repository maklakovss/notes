package com.mss.notes.ui.base

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.mss.notes.R
import com.mss.notes.data.errors.NoAuthException
import kotlinx.android.synthetic.main.activity_main.*

private const val RC_SIGN_IN = 458

abstract class BaseActivity<T, S : BaseViewState<T>> : AppCompatActivity() {
    abstract val model: BaseViewModel<T, S>
    abstract val layoutRes: Int?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutRes?.let { setContentView(it) }
        model.getViewState().observe(
                this, Observer<S> { result ->
            result?.apply {
                data?.let { renderData(it) }
                error?.let { renderError(it) }
            }
        })
    }

    private fun renderError(error: Throwable) {
        when (error) {
            is NoAuthException -> startLoginActivity()
            else -> error.message?.let { showError(it) }
        }
    }

    private fun startLoginActivity() {
        val provides = listOf(
                AuthUI.IdpConfig.EmailBuilder().build(),
                AuthUI.IdpConfig.GoogleBuilder().build())

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setLogo(R.drawable.android_robot)
                        .setTheme(R.style.LoginStyle)
                        .setAvailableProviders(provides)
                        .build(),
                RC_SIGN_IN
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SIGN_IN && resultCode != Activity.RESULT_OK) {
            finish()
        }
    }

    abstract fun renderData(data: T)

    protected fun showError(error: String) {
        val snackbar = Snackbar.make(rv_notes, error, Snackbar.LENGTH_INDEFINITE)
        snackbar.setAction(R.string.ok_bth_title, { snackbar.dismiss() })
        snackbar.show()
    }
}