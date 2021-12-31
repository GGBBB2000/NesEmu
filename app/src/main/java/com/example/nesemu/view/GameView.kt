package com.example.nesemu.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class GameView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {
    private val screenWidth : Int = 256;
    private val screenHeight : Int = 240;
    private val screen : Bitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        this.drawView(canvas)
    }

    private fun drawView(canvas: Canvas) {
        val array = Array(256 * 240) {0xFF0000FF.toInt()}
        screen.setPixels(array.toIntArray(), 0, screenWidth, 0, 0, screenWidth, screenHeight)
        val bmp = Bitmap.createScaledBitmap(screen, screenWidth * 4, screenHeight * 4, false);
        canvas.drawBitmap(bmp, 0f, 0f, Paint())
    }
}