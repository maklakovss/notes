package com.mss.notes.ui.main

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mss.notes.R
import com.mss.notes.common.getColorInt
import com.mss.notes.data.entity.Note
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_note.*

class NotesRVAdapter(private val onItemClickListener: (Note) -> Unit)
    : RecyclerView.Adapter<NotesRVAdapter.ViewHolder>() {

    var notes: List<Note> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false))

    override fun getItemCount(): Int = notes.size

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) = viewHolder.bind(notes[position])

    inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(note: Note) {
            tv_title.text = note.title
            tv_text.text = note.text

            itemView.setBackgroundColor(note.color.getColorInt(itemView.context))
            itemView.setOnClickListener { onItemClickListener(note) }
        }
    }
}