package com.mss.notes.ui.splash

import com.mss.notes.data.NotesRepository
import com.mss.notes.data.errors.NoAuthException
import com.mss.notes.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class SplashViewModel(private val repository: NotesRepository) :
        BaseViewModel<Boolean>() {

    fun requestUser() = launch {
        repository.getCurrentUser()?.let { setData(true) } ?: setError(NoAuthException())
    }
}
