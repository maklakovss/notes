package com.mss.notes.common

import android.content.Context
import android.support.v4.content.ContextCompat
import com.mss.notes.R
import com.mss.notes.data.entity.Color

fun Color.getColorInt(context: Context) =
        ContextCompat.getColor(context, when (this) {
            Color.WHITE -> R.color.color_white
            Color.VIOLET -> R.color.color_violet
            Color.YELLOW -> R.color.color_yellow
            Color.RED -> R.color.color_red
            Color.PINK -> R.color.color_pink
            Color.GREEN -> R.color.color_green
            Color.BLUE -> R.color.color_blue
        })
