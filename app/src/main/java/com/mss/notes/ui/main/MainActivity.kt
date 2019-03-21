package com.mss.notes.ui.main

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.firebase.ui.auth.AuthUI
import com.mss.notes.R
import com.mss.notes.data.entity.Note
import com.mss.notes.ui.base.BaseActivity
import com.mss.notes.ui.note.NoteActivity
import com.mss.notes.ui.splash.SplashActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<List<Note>?, MainViewState>(), LogoutDialog.LogoutListener {

    override val viewModel: MainViewModel by lazy { ViewModelProviders.of(this).get(MainViewModel::class.java) }
    override val layoutRes: Int = R.layout.activity_main

    private lateinit var adapter: NotesRVAdapter

    companion object {
        fun getStartIntent(context: Context) = Intent(context, MainActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbarMain)

        adapter = NotesRVAdapter({
            openNoteActivity(it)
        })

        rv_notes.adapter = adapter
        rv_notes.layoutManager = LinearLayoutManager(this)

        fab.setOnClickListener { openNoteActivity(null) }
    }

    override fun renderData(data: List<Note>?) {
        if (data == null) return
        adapter.notes = data
    }

    private fun openNoteActivity(note: Note?) {
        val intent = NoteActivity.getStartIntent(this, note?.id)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean =
            MenuInflater(this).inflate(R.menu.menu_main, menu).let { true }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean =
            when (item?.itemId) {
                R.id.logout -> showLogoutDialog().let { true }
                else -> false
            }

    private fun showLogoutDialog() =
            supportFragmentManager.findFragmentByTag(LogoutDialog.TAG)
                    ?: LogoutDialog.createInstance().show(supportFragmentManager, LogoutDialog.TAG)

    override fun onLogout() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener {
                    startActivity(Intent(this, SplashActivity::class.java))
                    finish()
                }
    }
}
