package com.darius.luftballon.Actors

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.scenes.scene2d.Actor
import com.darius.luftballon.Activities.Simulation
import com.darius.luftballon.Utilities.Physics

class Monitoring(var sim: Simulation, private var physics: Physics): Actor() {

    private val screenHeight = Gdx.graphics.height //TODO DELETE
    private val screenWidth = Gdx.graphics.width //TODO DELETE
    private var characters: String = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|/?-+=()*&.;,{}´`:'<>абвгдеёжзийклмнопрстуфхцчшщьыъэюяАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЧЦШЩЪЫЬЭЮЯöäüÜÖÄ"

    private val back = Sprite(Texture("monitorback.png"))
    private var font: BitmapFont? = null
    private var generator = FreeTypeFontGenerator(Gdx.files.internal("Fonts/fontGoose.ttf"))
    private var parameter = FreeTypeFontGenerator.FreeTypeFontParameter()
    private var rows: List<Float> = listOf(975f, 865f, 765f, 620f, 415f, 215f)
    private var columns: List<Float> = listOf(70f,390f)

    private var temp: Float? = null //TODO DELETE

    init{
        parameter.size = 50
        parameter.characters = characters
        font = BitmapFont()
        font?.setColor(1f, 0f, 0f, 1f);
        font = generator.generateFont(parameter)
        generator.dispose()
        back.setScale(1.25f)
        back.x = 80f
        back.y = 105f
    }
    override fun draw(batch: Batch?, parentAlpha: Float) {
        back.draw(batch)
        val accel = (physics.getAcceleration()!! * 1e2).toInt().div(1e2)
        val speed = (sim.getSpeed()!! * 1e2).toInt().div(1e2)
        font?.draw(batch,"Высота:",columns[0],rows[0])
        font?.draw(batch, sim.getFlightHeight()?.toInt().toString(),columns[1] - 80f,rows[0])
        font?.draw(batch, "Ускорение:",columns[0] ,rows[1])
        font?.draw(batch, accel.toString(),columns[1] ,rows[1])
        font?.draw(batch, "Скорость:",columns[0],rows[2])
        font?.draw(batch, speed.toString(),columns[1] ,rows[2])

        font?.draw(batch, "Масса:",columns[0],rows[3])
        font?.draw(batch, sim.getMass()!!.toInt().toString(),columns[1],rows[3])
        font?.draw(batch,"Объём:" ,columns[0],rows[4])
        font?.draw(batch, sim.getVolume()!!.toInt().toString(),columns[1],rows[4])
        font?.draw(batch,"Темпер.:" ,columns[0],rows[5])
        font?.draw(batch, sim.getGasT()!!.toString(),columns[1],rows[5])
    }

    override fun act(delta: Float) {
        //TODO USELESS
    }
}