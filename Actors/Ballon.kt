package com.darius.luftballon.Actors

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.scenes.scene2d.Actor
import com.darius.luftballon.Utilities.Physics
import com.darius.luftballon.Utilities.SPenInput

class Ballon: Actor(), SPenInput{
        //button_only
        private var mass = 500f
        private var volume = mass / 1.225f
        private var speed = 0f
        private var gasT = 40f
        private var maxSpeed = 50f
        private var flightHeight = 0f
        private var gasn = 500f /1.225f * 101325f  /8.31f/ (273f + 40f)

        private var border = 400f

        private val resizeRatio = 0.65f
        private var image = Sprite(Texture("designer_soul.png"))
        private val screenHeight = Gdx.graphics.height
        private val screenWidth = Gdx.graphics.width
        private var physics: Physics? = null

        private var obj: Ballon? = null

        fun getInstance(): Ballon{
            if(obj==null)
                obj = Ballon()
            return obj!!
        }

        fun setPhysics(physics_: Physics){
            physics = physics_
        }

        init {
            image.setScale(resizeRatio)
            image.x = screenWidth.toFloat() / 4f
            image.y = -80f
            setBounds(image.x, image.y, image.width, image.height)
            massInc()
        }

        override fun draw(batch: Batch?, parentAlpha: Float) {
            image.draw(batch)
        }

        override fun act(delta: Float) {
            speed = physics!!.balloonStep(this, delta)
            flightHeightCalc()
            when(flightHeight < border) {
                true -> {
                    image.y = flightHeight * (screenHeight.toFloat()/2f/border) - 80f
                    image.x = screenWidth.toFloat()/4f + screenWidth.toFloat()/4f * (flightHeight/border)
                    //println(" ${flightHeight} + $border+ ${image.x}")
                }
                false -> {
                    if (image.y > screenHeight - image.height) image.y = screenHeight - image.height
                    if (image.y < screenHeight/2 - 80f ) image.y = screenHeight / 2 - 80f
                    //if (image.y != screenHeight - image.height && image.y != screenHeight/2 - 80f)
                    image.y += speed * 2f
                    image.x = screenWidth.toFloat()/2f
                }
            }
            setBounds(image.x, image.y, image.width, image.height)
        }

        fun flightHeightCalc(){
            flightHeight += when (speed > maxSpeed) {
                true -> maxSpeed
                false -> speed
            }
            if(flightHeight < 0) {
                flightHeight = 0f
                speed = 0f
            }
        }

        fun massInc(){
            mass+=20
            if( mass > 1000f) mass = 1000f
        }
        fun massDec(){
            mass-=20
            if( mass < 200f) mass = 200f
        }
        fun volumeInc(){
            volume+=200
            if( volume > 5000f) volume = 5000f
            gasn = 101325f *volume /8.31f/ (gasT + 273f)
        }
        fun volumeDec(){
            volume-=200
            if( volume < 400f) volume = 400f
            gasn = 101325f *volume /8.31f/ (gasT + 273f)
        }
        fun tempInc(){
            gasT+=5
            if( gasT > 140f) gasT = 140f
            volume = gasn * 8.31f * (gasT + 273f) / 101325f
            if( volume > 5000f){
                volume = 5000f
                gasn = 101325f *volume /8.31f/ (gasT + 273f)
            }
        }
        fun tempDec(){
            gasT-=5
            if( gasT < 35f) gasT = 35f
            volume = gasn * 8.31f * (gasT + 273f) / 101325f
            if( volume < 400f){
                volume = 400f
                gasn = 101325f * volume /8.31f/ (gasT + 273f)
            }
        }

        override fun action(type: Int) {
            when(type){
                1 -> massInc()
                2 -> massDec()
                3 -> volumeInc()
                4 -> volumeDec()
                5 -> tempInc()
                6 -> tempDec()
            }
        }

        fun setParams(settingsMass: Float, settingsVolume: Float, settingsTemp: Float){
            mass = settingsMass
            volume = settingsVolume
            gasT = settingsTemp
            gasn = volume * 101325f  /8.31f/ (273f + gasT)
            image.x = screenWidth.toFloat()/2f
            image.y = (screenHeight * 1.5f - image.height  - 80f) / 2f
            var height: Float = 0f

            while(physics!!.balloonAcceleration(height,volume,mass) > 0.01f) height+=1
            flightHeight = height
        }

        fun getFlightHeight() = flightHeight
        fun getSpeed() = speed
        fun getVolume() = volume
        fun getMass() = mass
        fun getGasT() = gasT
        fun getGasn() = gasn
}