package com.mss.notes.data.provider

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.mss.notes.data.entity.Note
import com.mss.notes.data.entity.NoteResult

private const val NOTES_COLLECTION = "notes"

class FireStoreProvider : RemoteDataProvider {

    private val TAG = "${FireStoreProvider::class.java.simpleName} :"

    private val db = FirebaseFirestore.getInstance()
    private val notesReference = db.collection(NOTES_COLLECTION)

    override fun getNoteById(id: String): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()

        notesReference.document(id)
                .get()
                .addOnSuccessListener { result.value = NoteResult.Success(it.toObject(Note::class.java)) }
                .addOnFailureListener { result.value = NoteResult.Error(it) }

        return result
    }

    override fun saveNote(note: Note): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()

        notesReference.document(note.id)
                .set(note)
                .addOnSuccessListener {
                    Log.d(TAG, "Note $note is saved")
                    result.value = NoteResult.Success(note)
                }
                .addOnFailureListener {
                    Log.d(TAG, "Error saving note $note, message:${it.message}")
                    result.value = NoteResult.Error(it)
                }

        return result
    }

    override fun subscribeToAllNotes(): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()

        notesReference.addSnapshotListener { querySnapshot, exception ->
            if (exception != null) {
                result.value = NoteResult.Error(exception)
            } else if (querySnapshot != null) {
                val notes = mutableListOf<Note>()
                for (doc in querySnapshot) {
                    notes.add(doc.toObject(Note::class.java))
                }
                result.value = NoteResult.Success(notes)
            }
        }

        return result
    }
}