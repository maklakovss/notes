package com.mss.notes.data.provider

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mss.notes.data.entity.Note
import com.mss.notes.data.entity.Result
import com.mss.notes.data.entity.User
import com.mss.notes.data.errors.NoAuthException

private const val NOTES_COLLECTION = "notes"
private const val USERS_COLLECTION = "users"

class FireStoreProvider(private val firebaseAuth: FirebaseAuth, private val db: FirebaseFirestore) : RemoteDataProvider {

    private val TAG = "${FireStoreProvider::class.java.simpleName} :"

    private val currentUser
        get() = firebaseAuth.currentUser

    private fun getUserNotesCollection() = currentUser?.let {
        db.collection(USERS_COLLECTION).document(it.uid).collection(NOTES_COLLECTION)
    } ?: throw NoAuthException()

    override fun getNoteById(id: String): LiveData<Result> =
            MutableLiveData<Result>().apply {
                try {
                    getUserNotesCollection().document(id)
                            .get()
                            .addOnSuccessListener { value = Result.Success(it.toObject(Note::class.java)) }
                            .addOnFailureListener { throw it }
                } catch (e: Throwable) {
                    value = Result.Error(e)
                }
            }

    override fun saveNote(note: Note): LiveData<Result> =
            MutableLiveData<Result>().apply {
                try {
                    getUserNotesCollection().document(note.id)
                            .set(note)
                            .addOnSuccessListener {
                                value = Result.Success(note)
                            }
                            .addOnFailureListener {
                                Log.d(TAG, "Error saving note $note, message:${it.message}")
                                throw it
                            }
                } catch (e: Throwable) {
                    value = Result.Error(e)
                }
            }

    override fun subscribeToAllNotes(): LiveData<Result> =
            MutableLiveData<Result>().apply {
                try {
                    getUserNotesCollection().addSnapshotListener { querySnapshot, exception ->
                        value = exception?.let { Result.Error(exception) }
                                ?: querySnapshot?.let {
                                    Result.Success(it.documents.map { it.toObject(Note::class.java) })
                                }
                    }
                } catch (e: Throwable) {
                    value = Result.Error(e)
                }
            }

    override fun getCurrentUser(): LiveData<User?> =
            MutableLiveData<User?>().apply {
                value = currentUser?.let { User(it.displayName ?: "", it.email ?: "") }
            }

    override fun deleteNode(noteId: String): LiveData<Result> = MutableLiveData<Result>().apply {
        getUserNotesCollection().document(noteId).delete()
                .addOnSuccessListener { value = Result.Success(null) }
                .addOnFailureListener { value = Result.Error(it) }
    }
}