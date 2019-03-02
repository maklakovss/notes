package com.mss.notes.ui.main

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.mss.notes.data.NotesRepository

class MainViewModel : ViewModel() {

    val viewStateLiveData: MutableLiveData<MainViewState> = MutableLiveData()

    init {
        NotesRepository.notesLiveData.observeForever {
            viewStateLiveData.value = viewStateLiveData.value?.copy(notes = it!!)
                    ?: MainViewState(it!!)
        }
    }
}