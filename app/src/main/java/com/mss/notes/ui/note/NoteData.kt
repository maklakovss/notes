package com.mss.notes.ui.note

import com.mss.notes.data.entity.Note

data class NoteData(val isDeleted: Boolean = false, val note: Note? = null)