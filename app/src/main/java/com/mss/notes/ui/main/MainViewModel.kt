package com.mss.notes.ui.main

import android.arch.lifecycle.Observer
import android.support.annotation.VisibleForTesting
import com.mss.notes.data.NotesRepository
import com.mss.notes.data.entity.Note
import com.mss.notes.data.entity.Result
import com.mss.notes.ui.base.BaseViewModel

class MainViewModel(val repository: NotesRepository)
    : BaseViewModel<List<Note>?, MainViewState>() {

    private val repositoryNotes = repository.getNotes()

    private val notesObserver = Observer<Result> {
        if (it != null) {
            when (it) {
                is com.mss.notes.data.entity.Result.Success<*> -> {
                    viewStateLiveData.value = MainViewState(notes = it.data as?List<Note>)
                }
                is com.mss.notes.data.entity.Result.Error -> {
                    viewStateLiveData.value = MainViewState(error = it.error)
                }
            }
        }
    }

    init {
        viewStateLiveData.value = MainViewState()
        repositoryNotes.observeForever(notesObserver)
    }

    @VisibleForTesting
    public override fun onCleared() {
        repositoryNotes.removeObserver(notesObserver)
    }
}