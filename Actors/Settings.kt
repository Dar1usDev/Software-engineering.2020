package com.darius.luftballon.Actors

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.scenes.scene2d.Actor

class Settings(var ctrl: Control): Actor() {

    private val screenHeight = Gdx.graphics.height
    private val screenWidth = Gdx.graphics.width
    private var texture: Texture = Texture("Buttons/buttonUp.png")
    private var imgScale: Float = 0.8f
    ///settings
    private var settingsOpened: Int = -1
    private var settings: Sprite = Sprite(Texture("Buttons/settings.png"))
    private var apply: Sprite = Sprite(Texture("Buttons/apply.png"))
    private var buttons: List<Sprite> = listOf( Sprite(texture), Sprite(texture),
            Sprite(texture), Sprite(texture),
            Sprite(texture), Sprite(texture))
    private var settingsRows: List<Float> = listOf(720f, 522f, 325f) // +- 200
    private var settingsColumns: List<Float> = listOf(1515f,1745f) // +- 225
    private var settingsEnable: MutableList<Float> = mutableListOf(1f,1f,1f,1f,1f,1f)
    private var settingsMass = 500f
    private var settingsVolume = 1000f
    private var settingsTemp = 50f
    ///settings

    ///font
    private var characters: String = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|/?-+=()*&.;,{}´`:'<>абвгдеёжзийклмнопрстуфхцчшщьыъэюяАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЧЦШЩЪЫЬЭЮЯöäüÜÖÄ"
    private val back = Sprite(Texture("settingsback.png"))
    private var font: BitmapFont? = null
    private var generator = FreeTypeFontGenerator(Gdx.files.internal("Fonts/fontGoose.ttf"))
    private var parameter = FreeTypeFontGenerator.FreeTypeFontParameter()
    ///font

    init{
        settings.x = screenWidth * 13.5f / 16f
        settings.y = screenHeight * 7f / 8f
        settings.setScale(1.3f)
        settings.setBounds(settings.x, settings.y,settings.width,settings.height)
        back.setScale(1.35f)
        back.x = 1510f
        back.y = 310f
        apply.x = screenWidth * 14.25f / 16f
        apply.y = screenHeight * 3.75f / 8f
        apply.setScale(1.3f)
        apply.setBounds(apply.x, apply.y,apply.width,apply.height)
        settingsOpened = -1
        for( i in buttons.indices step 2) {
            buttons[i].x = settingsColumns[1] - 25f
            buttons[i].y = settingsRows[i/2]
            buttons[i+1].x = settingsColumns[0] - 25f
            buttons[i+1].y = settingsRows[i/2]
            buttons[i].setScale(imgScale)
            buttons[i+1].setScale(imgScale)
            buttons[i].setBounds(buttons[i].x,buttons[i].y,buttons[i].width,buttons[i].height)
            buttons[i+1].setBounds(buttons[i+1].x,buttons[i+1].y,buttons[i+1].width,buttons[i+1].height)
            buttons[i+1].rotation = 180f
        }
        parameter.size = 50
        parameter.characters = characters
        font = BitmapFont()
        font?.setColor(1f, 0f, 0f, 1f);
        font = generator.generateFont(parameter)
        generator.dispose()
        changeSets(0)
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        settings.draw(batch)
        if(settingsOpened == -1) return
        back.draw(batch, 1f)
        apply.draw(batch)
        font?.draw(batch, "Масса:",settingsColumns[0],settingsRows[0])
        font?.draw(batch, "${settingsMass.toInt()}",settingsColumns[1],settingsRows[0])
        font?.draw(batch,"Объём:" ,settingsColumns[0],settingsRows[1])
        font?.draw(batch,"${settingsVolume.toInt()}",settingsColumns[1],settingsRows[1])
        font?.draw(batch,"Темпер.:" ,settingsColumns[0],settingsRows[2])
        font?.draw(batch, "${settingsTemp.toInt()}",settingsColumns[1],settingsRows[2])
        for (i in buttons.indices)
            buttons[i].draw(batch,settingsEnable[i])
    }

    override fun act(delta: Float) {

    }

    fun check(x: Int, y: Int){
        var hit: Boolean = false
        when{
            ((x in settings.x.toInt()..(settings.x+settings.width).toInt())&&(y in settings.y.toInt()..(settings.y+settings.height).toInt())) -> { changeState(); hit = true }
            ((x in apply.x.toInt()..(apply.x+apply.width).toInt())&&(y in apply.y.toInt()..(apply.y+apply.height).toInt())) -> setParams()
            ((x in buttons[0].x.toInt()..(buttons[0].x+buttons[0].width).toInt())&&(y in buttons[0].y.toInt()..(buttons[0].y+buttons[0].height).toInt())) -> { changeSets(1); hit = true}
            ((x in buttons[1].x.toInt()..(buttons[1].x+buttons[1].width).toInt())&&(y in buttons[1].y.toInt()..(buttons[1].y+buttons[1].height).toInt())) -> { changeSets(2); hit = true}
            ((x in buttons[2].x.toInt()..(buttons[2].x+buttons[2].width).toInt())&&(y in buttons[2].y.toInt()..(buttons[2].y+buttons[2].height).toInt())) -> { changeSets(3); hit = true}
            ((x in buttons[3].x.toInt()..(buttons[3].x+buttons[3].width).toInt())&&(y in buttons[3].y.toInt()..(buttons[3].y+buttons[3].height).toInt())) -> { changeSets(4); hit = true}
            ((x in buttons[4].x.toInt()..(buttons[4].x+buttons[4].width).toInt())&&(y in buttons[4].y.toInt()..(buttons[4].y+buttons[4].height).toInt())) -> { changeSets(5); hit = true}
            ((x in buttons[5].x.toInt()..(buttons[5].x+buttons[5].width).toInt())&&(y in buttons[5].y.toInt()..(buttons[5].y+buttons[5].height).toInt())) -> { changeSets(6); hit = true}
        }
        if(!hit) settingsOpened = -1
    }

    fun changeState(){ settingsOpened *= -1 }

    fun changeSets(type: Int){
        if (settingsOpened == -1 && type != 0) return
        when (type){
            0 -> println()
            1 -> settingsMass += 20
            2 -> settingsMass -= 20
            3 -> settingsVolume += 200
            4 -> settingsVolume -= 200
            5 -> settingsTemp += 5
            6 -> settingsTemp -= 5
        }

        if( settingsMass > 1000f) settingsMass = 1000f
        if( settingsMass < 300f) settingsMass = 300f
        if( settingsVolume > 5000f) settingsVolume = 5000f
        if( settingsVolume < 400f) settingsVolume = 400f
        if( settingsTemp > 140f) settingsTemp = 140f
        if( settingsTemp < 35f) settingsTemp = 35f

        when(settingsMass){
            in 300.1f..999.9f -> { settingsEnable[0] = 1f; settingsEnable[1] = 1f}
            300f -> settingsEnable[1] = 0.5f
            1000f -> settingsEnable[0] = 0.5f
        }
        when(settingsVolume){
            in 400.1f..4999.9f -> { settingsEnable[2] = 1f; settingsEnable[3] = 1f}
            400f -> settingsEnable[3] = 0.5f
            5000f -> settingsEnable[2] = 0.5f
        }
        when(settingsTemp){
            in 35.1f..139.9f -> { settingsEnable[4] = 1f; settingsEnable[5] = 1f}
            35f -> settingsEnable[5] = 0.5f
            140f -> settingsEnable[4] = 0.5f
        }
    }

    fun setParams(){ ctrl.ballon.setParams(settingsMass, settingsVolume, settingsTemp) }
}