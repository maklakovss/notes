package com.mss.notes.data

import com.mss.notes.data.entity.Note
import com.mss.notes.data.provider.FireStoreProvider
import com.mss.notes.data.provider.RemoteDataProvider

object NotesRepository {

    private val remoteProvider: RemoteDataProvider = FireStoreProvider()

    fun getNotes() = remoteProvider.subscribeToAllNotes()

    fun saveNote(note: Note) = remoteProvider.saveNote(note)

    fun getNoteById(id: String) = remoteProvider.getNoteById(id)

    fun getCurrentUser() = remoteProvider.getCurrentUser()
}

