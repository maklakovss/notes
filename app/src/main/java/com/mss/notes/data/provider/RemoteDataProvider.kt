package com.mss.notes.data.provider

import android.arch.lifecycle.LiveData
import com.mss.notes.data.entity.Note
import com.mss.notes.data.entity.Result
import com.mss.notes.data.entity.User

interface RemoteDataProvider {

    fun subscribeToAllNotes(): LiveData<Result>

    fun getNoteById(id: String): LiveData<Result>

    fun saveNote(note: Note): LiveData<Result>

    fun getCurrentUser(): LiveData<User?>

    fun deleteNode(noteId: String): LiveData<Result>
}