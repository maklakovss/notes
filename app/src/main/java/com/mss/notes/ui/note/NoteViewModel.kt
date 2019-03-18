package com.mss.notes.ui.note

import android.arch.lifecycle.Observer
import com.mss.notes.data.NotesRepository
import com.mss.notes.data.entity.Note
import com.mss.notes.data.entity.NoteResult
import com.mss.notes.ui.base.BaseViewModel

class NoteViewModel(val repository: NotesRepository = NotesRepository)
    : BaseViewModel<Note?, NoteViewState>() {

    private var pendingNote: Note? = null

    fun saveChanges(note: Note) {
        pendingNote = note
    }

    override fun onCleared() {
        if (pendingNote != null) {
            repository.saveNote(pendingNote!!)
        }
    }

    fun loadNote(noteId: String) {
        repository.getNoteById(noteId).observeForever(object : Observer<NoteResult> {
            override fun onChanged(t: NoteResult?) {
                if (t == null) return
                when (t) {
                    is NoteResult.Success<*> -> viewStateLiveData.value = NoteViewState(note = t.data as? Note)
                    is NoteResult.Error -> viewStateLiveData.value = NoteViewState(error = t.error)
                }
            }
        })
    }
}