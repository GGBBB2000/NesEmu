package com.example.nesemu

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import com.example.nesemu.nes.JoyPad
import com.example.nesemu.nes.cartridge.Cartridge
import com.example.nesemu.nes.Nes
import com.example.nesemu.view.JoyPadTouchEventListener
import com.example.nesemu.view.ScreenView
import java.util.*
import kotlin.concurrent.timerTask

class MainActivity : AppCompatActivity() {
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //val romData = applicationContext.assets.open("rom/helloworld/sample1/sample1.nes").readBytes()
        //val romData = applicationContext.assets.open("rom/mapwalker/mapwalker/MapWalker.nes").readBytes()
        val romData = applicationContext.assets.open("rom/giko008.nes").readBytes()
        //val romData = applicationContext.assets.open("rom/nestest.nes").readBytes()
        val cartridge = Cartridge.getCartridge(romData)
        val nes = Nes(cartridge)
        val screen = nes.getScreen()
        val view = findViewById<ScreenView>(R.id.screenView)
        val timer = Timer()
        val handler = Handler(Looper.getMainLooper())
        var fpsCount = 0
        timer.scheduleAtFixedRate(timerTask {
            handler.post {
                findViewById<TextView>(R.id.fpsView).text = "fps: $fpsCount"
                fpsCount = 0
            }
        }, 0, 1000)

        screen.isReady.observe(this, Observer {
            view.updateScreenData(screen.data)
        })

        findViewById<Button>(R.id.reset).setOnClickListener { nes.reset() }

        findViewById<Button>(R.id.run).setOnClickListener {
            timer.scheduleAtFixedRate(timerTask {
                nes.run()
                fpsCount++
            }, 0, 16)
        }

        findViewById<Button>(R.id.aButton).setOnTouchListener(JoyPadTouchEventListener(JoyPad.Button.A, nes.joyPad))
        findViewById<Button>(R.id.bButton).setOnTouchListener(JoyPadTouchEventListener(JoyPad.Button.B, nes.joyPad))
        findViewById<Button>(R.id.selectButton).setOnTouchListener(JoyPadTouchEventListener(JoyPad.Button.SELECT, nes.joyPad))
        findViewById<Button>(R.id.startButton).setOnTouchListener(JoyPadTouchEventListener(JoyPad.Button.START, nes.joyPad))
        findViewById<Button>(R.id.upButton).setOnTouchListener(JoyPadTouchEventListener(JoyPad.Button.UP, nes.joyPad))
        findViewById<Button>(R.id.downButton).setOnTouchListener(JoyPadTouchEventListener(JoyPad.Button.DOWN, nes.joyPad))
        findViewById<Button>(R.id.leftButton).setOnTouchListener(JoyPadTouchEventListener(JoyPad.Button.LEFT, nes.joyPad))
        findViewById<Button>(R.id.rightButton).setOnTouchListener(JoyPadTouchEventListener(JoyPad.Button.RIGHT, nes.joyPad))
    }
}