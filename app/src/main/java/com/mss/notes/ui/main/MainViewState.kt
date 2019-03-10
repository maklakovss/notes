package com.mss.notes.ui.main

import com.mss.notes.data.entity.Note
import com.mss.notes.ui.base.BaseViewState

class MainViewState(notes: List<Note>? = null, error: Throwable? = null)
    : BaseViewState<List<Note>?>(notes, error)