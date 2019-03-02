package com.mss.notes.ui.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.mss.notes.R
import com.mss.notes.data.entity.Note
import com.mss.notes.ui.note.NoteActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainViewModel
    lateinit var adapter: NotesRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbarMain)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        rv_notes.layoutManager = LinearLayoutManager(this)
        adapter = NotesRVAdapter({
            openNoteActivity(it)
        })

        rv_notes.adapter = adapter

        viewModel.viewStateLiveData.observe(this, Observer<MainViewState> { t -> t?.let { adapter.notes = it.notes } })
        fab.setOnClickListener { openNoteActivity(null) }
    }

    private fun openNoteActivity(note: Note?) {
        val intent = NoteActivity.getStartIntent(this, note)
        startActivity(intent)
    }
}
