package com.mss.notes.data.provider

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mss.notes.data.entity.Note
import com.mss.notes.data.entity.NoteResult
import com.mss.notes.data.entity.User
import com.mss.notes.data.errors.NoAuthException

private const val NOTES_COLLECTION = "notes"
private const val USERS_COLLECTION = "users"

class FireStoreProvider : RemoteDataProvider {

    private val TAG = "${FireStoreProvider::class.java.simpleName} :"

    private val db = FirebaseFirestore.getInstance()

    private val currentUser
        get() = FirebaseAuth.getInstance().currentUser

    private fun getUserNotesCollection() = currentUser?.let {
        db.collection(USERS_COLLECTION).document(it.uid).collection(NOTES_COLLECTION)
    } ?: throw NoAuthException()

    override fun getNoteById(id: String): LiveData<NoteResult> =
            MutableLiveData<NoteResult>().apply {
                try {
                    getUserNotesCollection().document(id)
                            .get()
                            .addOnSuccessListener { value = NoteResult.Success(it.toObject(Note::class.java)) }
                            .addOnFailureListener { throw it }
                } catch (e: Throwable) {
                    value = NoteResult.Error(e)
                }
            }

    override fun saveNote(note: Note): LiveData<NoteResult> =
            MutableLiveData<NoteResult>().apply {
                getUserNotesCollection().document(note.id)
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
                getUserNotesCollection().addSnapshotListener { querySnapshot, exception ->
                    value = exception?.let {
                        NoteResult.Error(exception)
                    } ?: querySnapshot?.let {
                        NoteResult.Success(it.documents.map { it.toObject(Note::class.java) })
                    }
                }
            }

    override fun getCurrentUser(): LiveData<User?> =
            MutableLiveData<User?>().apply {
                value = currentUser?.let { User(it.displayName ?: "", it.email ?: "") }
            }
}