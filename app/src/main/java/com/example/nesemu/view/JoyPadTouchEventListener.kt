package com.example.nesemu.view

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import com.example.nesemu.nes.JoyPad

@SuppressLint("ClickableViewAccessibility")
class JoyPadTouchEventListener(private val button: JoyPad.Button, private val joyPad: JoyPad) : View.OnTouchListener {
    override fun onTouch(view: View, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                joyPad.setButtonState(button, true)
            }
            MotionEvent.ACTION_UP -> {
                joyPad.setButtonState(button, false)
            }
        }
        return false
    }
}