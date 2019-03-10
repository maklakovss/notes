package com.mss.notes.ui.note

import com.mss.notes.data.entity.Note
import com.mss.notes.ui.base.BaseViewState

class NoteViewState(note: Note? = null, error: Throwable? = null) : BaseViewState<Note?>(note, error)