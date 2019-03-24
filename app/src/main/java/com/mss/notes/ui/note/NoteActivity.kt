package com.mss.notes.ui.note

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import com.mss.notes.R
import com.mss.notes.common.DATE_TIME_FORMAT
import com.mss.notes.common.format
import com.mss.notes.common.getColorInt
import com.mss.notes.data.entity.Color
import com.mss.notes.data.entity.Note
import com.mss.notes.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_note.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.startActivity
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

private const val SAVE_DELAY = 2000L

class NoteActivity : BaseActivity<NoteViewState.Data, NoteViewState>() {

    override val model: NoteViewModel by viewModel()
    override val layoutRes: Int = R.layout.activity_note

    private var note: Note? = null
    private var color = Color.WHITE

    override fun renderData(data: NoteViewState.Data) {
        if (data.isDeleted) finish()
        this.note = data.note
        data.note?.let { color = it.color }
        initViews()
    }

    companion object {
        private val EXTRA_NOTE = NoteActivity::class.java.name + "extra.NOTE"

        fun start(context: Context, noteId: String?) =
                context.startActivity<NoteActivity>(EXTRA_NOTE to noteId)
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
                    lastChanged = Date(),
                    color = color)
                    ?: createNewNote()
            note?.let { model.saveChanges(it) }

        }, SAVE_DELAY)
    }

    private fun setToolbarColor(color: Color) {
        toolbarNote.setBackgroundColor(color.getColorInt(this))
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
            model.loadNote(it)
        } ?: let {
            supportActionBar?.title = getString(R.string.new_note_title)
        }

        colorPicker.onColorClickListener = {
            color = it
            setToolbarColor(it)
            triggerSaveNote()
        }

        setEditListener()
    }

    private fun initViews() {
        note?.run {
            supportActionBar?.title = lastChanged.format(DATE_TIME_FORMAT)
            toolbarNote.setBackgroundColor(color.getColorInt(this@NoteActivity))

            removeEditListener()
            et_title.setText(title)
            et_body.setText(text)
            setEditListener()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        android.R.id.home -> {
            onBackPressed()
            true
        }
        R.id.palette -> togglePalette().let { true }
        R.id.delete -> deleteNote().let { true }
        else -> super.onOptionsItemSelected(item)
    }

    private fun deleteNote() {
        alert {
            messageResource = R.string.delete_dialog_message
            negativeButton(R.string.delete_dialog_cancel) { dialog -> dialog.dismiss() }
            positiveButton(R.string.ok_bth_title) { dialog -> model.deleteNote() }
        }.show()
    }

    private fun togglePalette() {
        if (colorPicker.isOpen) {
            colorPicker.close()
        } else {
            colorPicker.open()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean = menuInflater.inflate(R.menu.menu_note, menu).let { true }

    private fun setEditListener() {
        et_title.addTextChangedListener(textChangeListener)
        et_body.addTextChangedListener(textChangeListener)
    }

    private fun removeEditListener() {
        et_title.removeTextChangedListener(textChangeListener)
        et_body.removeTextChangedListener(textChangeListener)
    }

    override fun onBackPressed() {
        if (colorPicker.isOpen) {
            colorPicker.close()
        } else {
            super.onBackPressed()
        }
    }
}
