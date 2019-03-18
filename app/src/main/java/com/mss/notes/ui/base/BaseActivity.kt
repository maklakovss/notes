package com.mss.notes.ui.base

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.mss.notes.R
import kotlinx.android.synthetic.main.activity_main.*

abstract class BaseActivity<T, S : BaseViewState<T>> : AppCompatActivity() {
    abstract val viewModel: BaseViewModel<T, S>
    abstract val layoutRes: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutRes)
        viewModel.getViewState().observe(
                this, Observer<S> {
            it?.apply {
                data?.apply { renderData(this) }
                error?.apply { renderError(this) }
            }
        })
    }

    private fun renderError(error: Throwable) {
        if (error.message != null) showError(error.message!!)
    }

    abstract fun renderData(data: T)

    protected fun showError(error: String) {
        val snackbar = Snackbar.make(rv_notes, error, Snackbar.LENGTH_INDEFINITE)
        snackbar.setAction(R.string.ok_bth_title, { snackbar.dismiss() })
        snackbar.show()
    }
}