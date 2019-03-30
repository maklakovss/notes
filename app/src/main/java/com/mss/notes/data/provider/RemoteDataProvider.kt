package com.mss.notes.data.provider

import com.mss.notes.data.entity.Note
import com.mss.notes.data.entity.Result
import com.mss.notes.data.entity.User
import kotlinx.coroutines.channels.ReceiveChannel

interface RemoteDataProvider {

    fun subscribeToAllNotes(): ReceiveChannel<Result>

    suspend fun getNoteById(id: String): Note

    suspend fun saveNote(note: Note): Note

    suspend fun getCurrentUser(): User?

    suspend fun deleteNode(noteId: String)
}