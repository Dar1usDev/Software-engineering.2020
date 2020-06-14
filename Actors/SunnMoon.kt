package com.darius.luftballon.Actors

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.scenes.scene2d.Actor
import kotlin.math.pow
import kotlin.math.sqrt

class SunnMoon(backStep: Float): Actor() {

    private val screenHeight = Gdx.graphics.height
    private val screenWidth = Gdx.graphics.width

    private var sun: Sprite = Sprite(Texture("sun.png"))
    private var moon: Sprite = Sprite(Texture("moon.png"))
    private val a: Float = screenWidth * (2100f /2190f)
    private val b: Float =  screenHeight *(1100f/1080f)
    private val x0: Float =  screenWidth * (1095f /2190f)
    private val y0: Float = -screenHeight * (300f/1080f)
    private val xStart: Float = screenWidth.toFloat() + 1f
    private val xStep: Float = screenWidth.toFloat() * backStep / 0.65f

    private var sunOn: Boolean = false
    private var moonOn: Boolean = false

    init{
        sun.x = xStart
        sun.setScale(0.3f)
        moon.x = xStart
        moon.setScale(0.4f)
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        if(sunOn)
            sun.draw(batch)
        if(moonOn)
            moon.draw(batch)
    }

    override fun act(delta: Float) {
        if (sunOn){
            sun.x -= xStep
            sun.y = yCalc(sun.x)
         }
        if (moonOn){
            moon.x -= xStep
            moon.y = yCalc(moon.x)
        }
        if(sun.x < -sun.width)
            sunOn = false
        if(moon.x < -moon.width)
            moonOn = false
    }

    private fun yCalc(x: Float) = sqrt(b.pow(2)-(x-x0).pow(2)*b.pow(2)/a.pow(2)) + y0

    fun sunOn(){
        if(sunOn) return
        sunOn = true
        sun.x = xStart
    }
    fun moonOn(){
        if(moonOn) return
        moonOn = true
        moon.x = xStart
    }
}
