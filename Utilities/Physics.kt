package com.darius.luftballon.Utilities

import com.darius.luftballon.Actors.Ballon
import kotlin.math.pow

class Physics {
    private var airDensity = 1.225f
    private var airM = 0.02896f
    private var windage = 0f

    private val airB = 0.2f //  TODO сопротивление проверить
    private val L = -0.0065f
    private val T0 = 288.15f
    private val p0 = 101325f
    private val gasR = 8.31f
    private val e = 2.71828f //  TODO useless
    private var p = 101325f

    private var g= 9.8f
    private val G = 6.672e-11f
    private val earthM = 5.973e24f
    private val earthR = 6.371e6f

    //temp // ball args sec
    private var acceleration: Float? = null
    private var mass: Float? = null
    private var volume: Float? = null
    private var temp: Float? = null
    private var Fheight: Float? = null
    private var speed: Float? = null
    private var weight: Float? = null
    private var Fa: Float? = null
    private var gasn: Float? = null
    //temp // ball args sec

    init{

    }

    fun gCalc(height: Float): Float{
        g = G * earthM / (earthR + height).pow(2f)
        return g
    }

    fun windageCalc(speed:Float) = airB*speed*speed

    fun pCalc(height: Float): Float{
        p = p0* (1f + L * height / T0).pow(-gCalc(height) * airM / gasR / L)
        return p
    }

    fun airDencityCalc(height: Float) : Float{
        val p = pCalc(height)
        val T = T0 + L*height
        airDensity = (p * airM/gasR/T)
        return airDensity
    }

    fun balloonAcceleration(height: Float, volume : Float, mass: Float) : Float {
        return gCalc(height) / mass * (airDencityCalc(height)*volume - mass )
    }

    fun balloonStep(ball: Ballon, delta: Float) : Float {
        acceleration = balloonAcceleration(ball.getFlightHeight(),ball.getVolume(),ball.getMass())
        windage = windageCalc(ball.getSpeed())
        mass = ball.getMass()
        Fheight = ball.getFlightHeight()
        temp = ball.getGasT()
        volume = ball.getVolume()
        speed = ball.getSpeed()
        weight = mass!! * gCalc(Fheight!!)
        Fa = airDensity* volume!! * gCalc(Fheight!!)
        gasn = ball.getGasn()

        return (acceleration!! - windage) * delta * 5 // TODO множитель
    }

    fun getAcceleration() = acceleration
}