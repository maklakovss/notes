package com.mss.notes.ui.note

import android.arch.lifecycle.ViewModel
import com.mss.notes.data.NotesRepository
import com.mss.notes.data.entity.Note

class NoteViewModel(private val repository: NotesRepository = NotesRepository) : ViewModel() {
    private var pendingNote: Note? = null

    fun saveChanges(note: Note) {
        pendingNote = note
    }

    override fun onCleared() {
        if (pendingNote != null) {
            repository.saveNote(pendingNote!!)
        }
    }
}