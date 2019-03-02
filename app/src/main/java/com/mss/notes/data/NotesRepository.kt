package com.mss.notes.data

import android.arch.lifecycle.MutableLiveData
import com.mss.notes.data.entity.Color
import com.mss.notes.data.entity.Note
import java.util.*

object NotesRepository {

    val notesLiveData = MutableLiveData<List<Note>>()

    private val notes: MutableList<Note> = mutableListOf(
            Note(UUID.randomUUID().toString(),
                    "Первая заметка",
                    "Текст первой заметки. Не очень длинный, но интересный",
                    Color.WHITE
            ),
            Note(UUID.randomUUID().toString(),
                    "Вторая заметка",
                    "Текст второй заметки. Не очень длинный, но интересный",
                    Color.BLUE
            ),
            Note(UUID.randomUUID().toString(),
                    "Третья заметка",
                    "Текст третьей заметки. Не очень длинный, но интересный",
                    Color.GREEN
            ),
            Note(UUID.randomUUID().toString(),
                    "Четвертая заметка",
                    "Текст четвертой заметки. Не очень длинный, но интересный",
                    Color.PINK
            ),
            Note(UUID.randomUUID().toString(),
                    "Пятая заметка",
                    "Текст пятой заметки. Не очень длинный, но интересный",
                    Color.RED
            ),
            Note(UUID.randomUUID().toString(),
                    "Шестая заметка",
                    "Текст шестой заметки. Не очень длинный, но интересный",
                    Color.YELLOW
            )
    )

    init {
        notesLiveData.value = notes
    }

    fun saveNote(note: Note) {
        addOrReplace(note)
        notesLiveData.value = notes
    }

    private fun addOrReplace(note: Note) {
        for (i in 0 until notes.size) {
            if (notes[i] == note) {
                notes[i] = note
                return
            }
        }
        notes.add(note)
    }

    fun delete(note: Note) {
        notes.remove(note)
    }
}

