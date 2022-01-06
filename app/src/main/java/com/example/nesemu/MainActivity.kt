package com.example.nesemu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.lifecycle.Observer
import com.example.nesemu.nes.cartridge.Cartridge
import com.example.nesemu.nes.Nes
import com.example.nesemu.view.ScreenView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //val romData = applicationContext.assets.open("rom/helloworld/sample1/sample1.nes").readBytes()
        //val romData = applicationContext.assets.open("rom/mapwalker/mapwalker/MapWalker.nes").readBytes()
        val romData = applicationContext.assets.open("rom/nestest.nes").readBytes()
        val cartridge = Cartridge.getCartridge(romData)
        val nes = Nes(cartridge)
        val screen = nes.getScreen()
        val view = findViewById<ScreenView>(R.id.screenView)
        screen.isReady.observe(this, Observer {
            view.updateScreenData(screen.data)
        })
        findViewById<Button>(R.id.reset).apply {
            this.setOnClickListener {
                nes.reset()
            }
        }
        findViewById<Button>(R.id.stepButton).apply {
            this.setOnClickListener {
                nes.run()
            }
        }
    }
}