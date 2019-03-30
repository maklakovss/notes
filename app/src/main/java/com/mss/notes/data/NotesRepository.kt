package com.mss.notes.data

import com.mss.notes.data.entity.Note
import com.mss.notes.data.provider.RemoteDataProvider

class NotesRepository(private val remoteProvider: RemoteDataProvider) {

    fun getNotes() = remoteProvider.subscribeToAllNotes()

    suspend fun saveNote(note: Note) = remoteProvider.saveNote(note)

    suspend fun getNoteById(id: String) = remoteProvider.getNoteById(id)

    suspend fun getCurrentUser() = remoteProvider.getCurrentUser()

    suspend fun deleteNote(noteId: String) = remoteProvider.deleteNode(noteId)
}

