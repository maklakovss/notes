package com.mss.notes.ui.main

import com.mss.notes.data.NotesRepository
import com.mss.notes.data.entity.Note
import com.mss.notes.data.entity.Result
import com.mss.notes.ui.base.BaseViewModel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch

class MainViewModel(val repository: NotesRepository)
    : BaseViewModel<List<Note>?>() {

    private val notesChannel = repository.getNotes()

    init {
        launch {
            notesChannel.consumeEach {
                when (it) {
                    is Result.Success<*> -> setData(it.data as? List<Note>)
                    is Result.Error -> setError(it.error)
                }
            }
        }
    }

    override fun onCleared() {
        notesChannel.cancel()
        super.onCleared()
    }
}