package com.darius.luftballon.Activities

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.darius.luftballon.Actors.*
import com.darius.luftballon.Utilities.Physics

class Simulation(var activity: StartActivity): Screen {

    private var physics: Physics = Physics()
    private var stars: Stars? = null
    private var sunnmoon : SunnMoon? = null
    private var farClouds: Clouds? = null
    var ballon: Ballon? = null
    private var nearClouds: Clouds? = null
    private var monitor: Monitoring? = null
    private var control: Control? = null
    private var ground: Ground? = null

    private var stage: Stage? = null
    private var camera : OrthographicCamera? = null
    private val screenHeight = Gdx.graphics.height
    private val screenWidth = Gdx.graphics.width

    private val backgroundStep: Float = 0.0005f
    private val blueMin = 0.3f
    private val midday =  (1f + blueMin) / 2f
    private val dif = 0.25f
    private var clearRed = 0f
    private var clearGreen = midday - dif
    private var clearBlue = midday + 0.0001f
    private var rising: Boolean = true
    var day: Boolean = false

    override fun show() {
        println("$screenWidth +$screenHeight") //TODO DELETE
        physics = Physics()
        camera = OrthographicCamera()
        camera!!.setToOrtho(false,screenWidth.toFloat(),screenHeight.toFloat())
        stage = Stage(ScreenViewport(camera))
        stars = Stars(this)
        sunnmoon = SunnMoon(backgroundStep)
        ballon = Ballon().getInstance()
        ballon!!.setPhysics(physics)
        ground = Ground(ballon!!)
        farClouds = Clouds(true, ballon!!)
        nearClouds = Clouds(false, ballon!!)
        monitor = Monitoring(this, physics)
        control = Control(ballon!!,activity)
        stage!!.addActor(stars)
        stage!!.addActor(sunnmoon)
        stage!!.addActor(farClouds)
        stage!!.addActor(ground)
        stage!!.addActor(ballon)
        stage!!.addActor(nearClouds)
        stage!!.addActor(monitor)
        stage!!.addActor(control)
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(clearRed, clearGreen, clearBlue, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        stage?.act(delta)
        stage?.draw()
        skyChange()
    }

    override fun dispose() {
        stage!!.dispose()
    }

    private fun skyChange(){
        when(rising){
            false ->{ clearGreen-=backgroundStep; clearBlue-=backgroundStep }
            true ->{ clearGreen+=backgroundStep; clearBlue+=backgroundStep}
        }
        if(clearBlue > 0.99f) // 0.99 0.3
            rising = false
        if(clearBlue < blueMin)
            rising = true
        when(clearBlue > midday){
            true ->{ day = true; sunnmoon!!.sunOn() }
            false ->{ day = false; sunnmoon!!.moonOn() }
        }
    }

    fun getFlightHeight() = ballon?.getFlightHeight()
    fun getSpeed() = ballon?.getSpeed()
    fun getVolume() = ballon?.getVolume()
    fun getMass() = ballon?.getMass()
    fun getGasT() = ballon?.getGasT()

    override fun pause() {
        // TODO CHECK
    }

    override fun resume() {
        // TODO CHECK
    }

    override fun resize(width: Int, height: Int) {
        // TODO CHECK
    }

    override fun hide() {
        // TODO CHECK
    }
}