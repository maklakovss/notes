package com.mss.notes.ui.main

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.MutableLiveData
import com.mss.notes.data.NotesRepository
import com.mss.notes.data.entity.Result
import io.mockk.mockk
import org.junit.Rule

class MainViewModelTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val mockRepository: NotesRepository = mockk()
    private val notesLiveData = MutableLiveData<Result>()
    private lateinit var viewModel: MainViewModel

//    @Before
//    fun setUp() {
//        every { mockRepository.getNotes() } returns notesLiveData
//        viewModel = MainViewModel(mockRepository)
//    }
//
//    @Test
//    fun `should call getNotes once`() {
//        verify(exactly = 1) { mockRepository.getNotes() }
//    }
//
//    @Test
//    fun `should return error`() {
//        var result: Throwable? = null
//        val testData = Throwable("error")
//        viewModel.getViewState().observeForever { result = it?.error }
//        notesLiveData.value = Result.Error(testData)
//        assertEquals(testData, result)
//    }
//
//    @Test
//    fun `should return Notes`() {
//        var result: List<Note>? = null
//        val testData = listOf(Note(id = "1"), Note(id = "2"))
//        viewModel.getViewState().observeForever { result = it?.data }
//        notesLiveData.value = Result.Success(testData)
//        assertEquals(testData, result)
//    }
//
//    @Test
//    fun `should remove observer`() {
//        viewModel.onCleared()
//        assertFalse(notesLiveData.hasObservers())
//    }
}