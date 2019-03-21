package com.mss.notes.data.provider

import android.arch.lifecycle.LiveData
import com.mss.notes.data.entity.Note
import com.mss.notes.data.entity.NoteResult
import com.mss.notes.data.entity.User

interface RemoteDataProvider {

    fun subscribeToAllNotes(): LiveData<NoteResult>

    fun getNoteById(id: String): LiveData<NoteResult>

    fun saveNote(note: Note): LiveData<NoteResult>

    fun getCurrentUser(): LiveData<User?>
}