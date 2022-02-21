package com.jh.dailypoem.ui.view

import android.graphics.Typeface
import android.view.View.*
import android.widget.TextView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.res.ResourcesCompat
import com.jh.dailypoem.R

@Composable
fun SongFamilyText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    textAlign: TextAlign? = null,
    isBold: Boolean = false,
    onClick: (() -> Unit)? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
) {
    AndroidView(
        modifier = modifier,
        factory = { context -> TextView(context) },
        update = {
            if (onClick != null) { it.setOnClickListener { onClick.invoke() } }

            if (color != Color.Unspecified) { it.setTextColor(color.toArgb()) }
            if (fontSize != TextUnit.Unspecified) { it.textSize = fontSize.value }
            if (textAlign != null){ it.textAlignment = textAlign.toStyle() }

            it.typeface = Typeface.create(
                ResourcesCompat.getFont(it.context, R.font.source_han_serif_cn),
                if (isBold) Typeface.BOLD else Typeface.NORMAL
            )

            it.text = text
            it.includeFontPadding = false
        }
    )
}

fun TextAlign?.toStyle(): Int {
    return when (this) {
        TextAlign.Left -> TEXT_ALIGNMENT_TEXT_START
        TextAlign.Right -> TEXT_ALIGNMENT_TEXT_END
        TextAlign.Center -> TEXT_ALIGNMENT_CENTER
        TextAlign.Start -> TEXT_ALIGNMENT_TEXT_START
        TextAlign.End -> TEXT_ALIGNMENT_TEXT_END
        else -> -1
    }
}