package com.mss.notes.data

import com.mss.notes.data.entity.Note
import com.mss.notes.data.provider.RemoteDataProvider

class NotesRepository(private val remoteProvider: RemoteDataProvider) {

    fun getNotes() = remoteProvider.subscribeToAllNotes()

    fun saveNote(note: Note) = remoteProvider.saveNote(note)

    fun getNoteById(id: String) = remoteProvider.getNoteById(id)

    fun getCurrentUser() = remoteProvider.getCurrentUser()

    fun deleteNote(noteId: String) = remoteProvider.deleteNode(noteId)
}

