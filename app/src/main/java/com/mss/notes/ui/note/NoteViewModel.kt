package com.mss.notes.ui.note

import com.mss.notes.data.NotesRepository
import com.mss.notes.data.entity.Note
import com.mss.notes.ui.base.BaseViewModel
import kotlinx.coroutines.launch

open class NoteViewModel(val repository: NotesRepository)
    : BaseViewModel<NoteData>() {

    private val currentNote: Note?
        get() = getViewState().poll()?.note

    fun saveChanges(note: Note) {
        setData(NoteData(note = note))
    }

    override fun onCleared() {
        launch {
            currentNote?.let { repository.saveNote(it) }
            super.onCleared()
        }
    }

    fun loadNote(noteId: String) = launch {
        try {
            repository.getNoteById(noteId).let {
                setData(NoteData(note = it))
            }
        } catch (e: Throwable) {
            setError(e)
        }
    }

    fun deleteNote() = launch {
        try {
            currentNote?.let { repository.deleteNote(it.id) }
            setData(NoteData(isDeleted = true))
        } catch (e: Throwable) {
            setError(e)
        }
    }
}