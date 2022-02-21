package com.jh.dailypoem.ui.view

import android.graphics.Bitmap
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.imageResource

class CustomPainter: Painter() {
    override val intrinsicSize: Size
        get() = TODO("Not yet implemented")

    override fun DrawScope.onDraw() {
        this.drawImage()

    }
}