package com.mss.notes.ui.main

import android.arch.lifecycle.Observer
import com.mss.notes.data.NotesRepository
import com.mss.notes.data.entity.Note
import com.mss.notes.data.entity.NoteResult
import com.mss.notes.ui.base.BaseViewModel

class MainViewModel(val repository: NotesRepository = NotesRepository)
    : BaseViewModel<List<Note>?, MainViewState>() {

    private val repositoryNotes = repository.getNotes()

    private val notesObserver = Observer<NoteResult> {
        if (it != null) {
            when (it) {
                is NoteResult.Success<*> -> {
                    viewStateLiveData.value = MainViewState(notes = it.data as?List<Note>)
                }
                is NoteResult.Error -> {
                    viewStateLiveData.value = MainViewState(error = it.error)
                }
            }
        }
    }

    init {
        viewStateLiveData.value = MainViewState()
        repositoryNotes.observeForever(notesObserver)
    }

    override fun onCleared() {
        repositoryNotes.removeObserver(notesObserver)
    }
}