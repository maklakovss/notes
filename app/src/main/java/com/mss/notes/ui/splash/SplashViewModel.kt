package com.mss.notes.ui.splash

import com.mss.notes.data.NotesRepository
import com.mss.notes.data.errors.NoAuthException
import com.mss.notes.ui.base.BaseViewModel

class SplashViewModel(private val repository: NotesRepository = NotesRepository) :
        BaseViewModel<Boolean?, SplashViewState>() {

    fun requestUser() {
        repository.getCurrentUser().observeForever {
            viewStateLiveData.value = if (it != null) {
                SplashViewState(isAuth = true)
            } else {
                SplashViewState(error = NoAuthException())
            }
        }
    }
}