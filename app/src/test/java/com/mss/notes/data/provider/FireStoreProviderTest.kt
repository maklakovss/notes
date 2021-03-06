package com.mss.notes.data.provider

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import com.mss.notes.data.entity.Note
import com.mss.notes.data.entity.Result
import com.mss.notes.data.errors.NoAuthException
import io.mockk.*
import junit.framework.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FireStoreProviderTest {
    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val mockDb = mockk<FirebaseFirestore>()
    private val mockAuth = mockk<FirebaseAuth>()
    private val mockCollection = mockk<CollectionReference>()
    private val mockUser = mockk<FirebaseUser>()
    private val mockDocument1 = mockk<DocumentSnapshot>()
    private val mockDocument2 = mockk<DocumentSnapshot>()
    private val mockDocument3 = mockk<DocumentSnapshot>()
    private val testNotes = listOf(Note(id = "1"), Note(id = "2"), Note(id = "3"))

    private val provider: FireStoreProvider = FireStoreProvider(mockAuth, mockDb)

    @Before
    fun setUp() {
        clearMocks(mockCollection, mockDocument1, mockDocument2, mockDocument3)

        every { mockAuth.currentUser } returns mockUser
        every { mockUser.uid } returns ""
        every { mockDb.collection(any()).document(any()).collection(any()) } returns mockCollection
        every { mockDocument1.toObject(Note::class.java) } returns testNotes[0]
        every { mockDocument2.toObject(Note::class.java) } returns testNotes[1]
        every { mockDocument3.toObject(Note::class.java) } returns testNotes[2]
    }

    @Test
    fun `should throw if not auth`() {
        var result: Any? = null
        every { mockAuth.currentUser } returns null
        provider.subscribeToAllNotes().observeForever {
            result = (it as?Result.Error)?.error
        }
        assertTrue(result is NoAuthException)
    }

    @Test
    fun `subscribeAllNotes return notes`() {
        var result: List<Note>? = null
        val slot = slot<EventListener<QuerySnapshot>>()
        val mockSnapshot = mockk<QuerySnapshot>()

        every { mockSnapshot.documents } returns listOf(mockDocument1, mockDocument2, mockDocument3)
        every { mockCollection.addSnapshotListener(capture(slot)) } returns mockk()

        provider.subscribeToAllNotes().observeForever {
            result = (it as?Result.Success<List<Note>>)?.data
        }

        slot.captured.onEvent(mockSnapshot, null)
        assertEquals(testNotes, result)
    }

    @Test
    fun `subscribeAllNotes return error`() {
        var result: Throwable? = null
        val slot = slot<EventListener<QuerySnapshot>>()
        val testError = mockk<FirebaseFirestoreException>()

        every { mockCollection.addSnapshotListener(capture(slot)) } returns mockk()

        provider.subscribeToAllNotes().observeForever { result = (it as? Result.Error)?.error }

        slot.captured.onEvent(null, testError)

        assertNotNull(result)
        assertEquals(testError, result)
    }

    @Test
    fun `saveNote calls document set`() {
        val mockDocumentReference: DocumentReference = mockk<DocumentReference>()
        every { mockCollection.document(testNotes[0].id) } returns mockDocumentReference
        provider.saveNote(testNotes[0])

        verify(exactly = 1) { mockDocumentReference.set(testNotes[0]) }
    }

    @Test
    fun `saveNote return Note`() {
        val mockDocumentReference: DocumentReference = mockk()
        val slot = slot<OnSuccessListener<in Void>>()
        var result: Note? = null

        every { mockCollection.document(testNotes[0].id) } returns mockDocumentReference
        every { mockDocumentReference.set(testNotes[0]).addOnSuccessListener(capture(slot)) } returns mockk()

        provider.saveNote(testNotes[0]).observeForever {
            result = (it as? Result.Success<Note>)?.data
        }
        slot.captured.onSuccess(null)

        assertNotNull(result)
        assertEquals(testNotes[0], result)
    }
}