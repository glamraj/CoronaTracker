package com.crazylegend.coronatracker

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.LinearLayout
import androidx.core.view.setMargins
import androidx.core.view.updateLayoutParams
import com.crazylegend.kotlinextensions.context.getCompatColor
import com.crazylegend.kotlinextensions.views.dp
import com.crazylegend.kotlinextensions.views.toColorSpan
import com.google.android.material.textview.MaterialTextView


/**
 * Created by crazy on 3/29/20 to long live and prosper !
 */


fun generateDivider(headerLayout: LinearLayout) {
    val view = View(headerLayout.context)
    view.setBackgroundColor(headerLayout.context.getCompatColor(R.color.dividerColor))
    headerLayout.addView(view)
    view.updateLayoutParams<LinearLayout.LayoutParams> {
        width = 1.dp
        height = LinearLayout.LayoutParams.MATCH_PARENT
        marginStart = 2.dp
        marginEnd = 2.dp
    }
}


fun Context.generateText(setText: String, headerLayout: LinearLayout, setWidth: Int = 80.dp) {

    val text = MaterialTextView(this)
    text.maxLines = 2

    if (setText.contains("+", true)){
        text.text = setText.toColorSpan(IntRange(0, setText.length), Color.RED)
    } else {
        text.text = setText
    }
    headerLayout.addView(text)
    text.updateLayoutParams<LinearLayout.LayoutParams> {
        width = setWidth
        height = LinearLayout.LayoutParams.MATCH_PARENT
        setMargins(5.dp)
    }
}