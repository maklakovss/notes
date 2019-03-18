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

    override fun getNoteById(id: String): LiveData<NoteResult> =
            MutableLiveData<NoteResult>().apply {
                notesReference.document(id)
                        .get()
                        .addOnSuccessListener { value = NoteResult.Success(it.toObject(Note::class.java)) }
                        .addOnFailureListener { value = NoteResult.Error(it) }
            }

    override fun saveNote(note: Note): LiveData<NoteResult> =
            MutableLiveData<NoteResult>().apply {
                notesReference.document(note.id)
                        .set(note)
                        .addOnSuccessListener {
                            Log.d(TAG, "Note $note is saved")
                            value = NoteResult.Success(note)
                        }
                        .addOnFailureListener {
                            Log.d(TAG, "Error saving note $note, message:${it.message}")
                            value = NoteResult.Error(it)
                        }
            }

    override fun subscribeToAllNotes(): LiveData<NoteResult> =
            MutableLiveData<NoteResult>().apply {
                notesReference.addSnapshotListener { querySnapshot, exception ->
                    value = exception?.let {
                        NoteResult.Error(exception)
                    } ?: querySnapshot?.let {
                        NoteResult.Success(it.documents.map { it.toObject(Note::class.java) })
                    }
                }
            }
}