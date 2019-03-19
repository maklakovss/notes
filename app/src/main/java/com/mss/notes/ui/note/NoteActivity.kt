package com.mss.notes.ui.note

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import com.mss.notes.R
import com.mss.notes.common.DATE_TIME_FORMAT
import com.mss.notes.common.format
import com.mss.notes.common.getColorInt
import com.mss.notes.data.entity.Note
import com.mss.notes.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_note.*
import java.util.*

private const val SAVE_DELAY = 2000L

class NoteActivity : BaseActivity<Note?, NoteViewState>() {

    override val viewModel: NoteViewModel by lazy { ViewModelProviders.of(this).get(NoteViewModel::class.java) }
    override val layoutRes: Int = R.layout.activity_note
    private var note: Note? = null

    override fun renderData(data: Note?) {
        this.note = data
        initViews()
    }

    companion object {
        private val EXTRA_NOTE = NoteActivity::class.java.name + "extra.NOTE"

        fun getStartIntent(context: Context, noteId: String?): Intent {
            val intent = Intent(context, NoteActivity::class.java)
            intent.putExtra(EXTRA_NOTE, noteId)
            return intent
        }
    }

    private val textChangeListener = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(p0: Editable?) {
            triggerSaveNote()
        }

    }

    private fun triggerSaveNote() {
        if (et_title.text == null || et_title.text!!.length < 3) return

        Handler().postDelayed({
            note = note?.copy(title = et_title.text.toString(),
                    text = et_body.text.toString(),
                    lastChanged = Date())
                    ?: createNewNote()
            if (note != null) viewModel.saveChanges(note!!)

        }, SAVE_DELAY)
    }

    private fun createNewNote(): Note = Note(UUID.randomUUID().toString(),
            et_title.text.toString(),
            et_body.text.toString())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val noteId = intent.getStringExtra(EXTRA_NOTE)
        setSupportActionBar(toolbarNote)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        noteId?.let {
            viewModel.loadNote(it)
        } ?: let {
            supportActionBar?.title = getString(R.string.new_note_title)
        }

        et_title.addTextChangedListener(textChangeListener)
        et_body.addTextChangedListener(textChangeListener)
    }

    private fun initViews() {
        note?.run {
            supportActionBar?.title = lastChanged.format(DATE_TIME_FORMAT)
            et_title.setText(title ?: "")
            et_body.setText(text ?: "")
            toolbarNote.setBackgroundColor(color.getColorInt(this@NoteActivity))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        android.R.id.home -> {
            onBackPressed()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

}
