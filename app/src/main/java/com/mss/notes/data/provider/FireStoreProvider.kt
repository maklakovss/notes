package com.mss.notes.data.provider

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.mss.notes.data.entity.Note
import com.mss.notes.data.entity.Result
import com.mss.notes.data.entity.User
import com.mss.notes.data.errors.NoAuthException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

private const val NOTES_COLLECTION = "notes"
private const val USERS_COLLECTION = "users"

class FireStoreProvider(private val firebaseAuth: FirebaseAuth, private val db: FirebaseFirestore) : RemoteDataProvider {

    private val TAG = "${FireStoreProvider::class.java.simpleName} :"

    private val currentUser
        get() = firebaseAuth.currentUser

    private fun getUserNotesCollection() = currentUser?.let {
        db.collection(USERS_COLLECTION).document(it.uid).collection(NOTES_COLLECTION)
    } ?: throw NoAuthException()

    override suspend fun getNoteById(id: String): Note =
            suspendCoroutine { continuation ->
                try {
                    getUserNotesCollection().document(id)
                            .get()
                            .addOnSuccessListener { continuation.resume(it.toObject(Note::class.java)!!) }
                            .addOnFailureListener { continuation.resumeWithException(it) }
                } catch (e: Throwable) {
                    continuation.resumeWithException(e)
                }
            }

    override suspend fun saveNote(note: Note): Note =
            suspendCoroutine { continuation ->
                try {
                    getUserNotesCollection().document(note.id)
                            .set(note)
                            .addOnSuccessListener { continuation.resume(note) }
                            .addOnFailureListener { continuation.resumeWithException(it) }
                } catch (e: Throwable) {
                    continuation.resumeWithException(e)
                }
            }

    override fun subscribeToAllNotes(): ReceiveChannel<Result> =
            Channel<Result>(Channel.CONFLATED).apply {
                var registration: ListenerRegistration? = null
                try {
                    registration = getUserNotesCollection().addSnapshotListener { querySnapshot, exception ->
                        val value = exception?.let { Result.Error(exception) }
                                ?: querySnapshot?.let {
                                    Result.Success(it.documents.map { it.toObject(Note::class.java) })
                                }
                        value?.let { offer(it) }
                    }
                } catch (e: Throwable) {
                    offer(Result.Error(e))
                }
                invokeOnClose { registration?.remove() }
            }

    override suspend fun getCurrentUser(): User? =
            suspendCoroutine { continuation ->
                continuation.resume(currentUser?.let {
                    User(it.displayName ?: "", it.email ?: "")
                })
            }

    override suspend fun deleteNode(noteId: String): Unit =
            suspendCoroutine { continuation ->
                getUserNotesCollection().document(noteId).delete()
                        .addOnSuccessListener { continuation.resume(Unit) }
                        .addOnFailureListener { continuation.resumeWithException(it) }
            }
}