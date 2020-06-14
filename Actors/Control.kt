package com.darius.luftballon.Actors

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.scenes.scene2d.Actor
import com.darius.luftballon.Activities.StartActivity
import com.darius.luftballon.Utilities.Input
import com.darius.luftballon.Utilities.Restart

class Control(var ballon: Ballon, var activity: StartActivity): Actor(),Restart {

    //other
    private val screenHeight = Gdx.graphics.height
    private val screenWidth = Gdx.graphics.width
    private var input: Input? = null
    private var texture: Texture = Texture("Buttons/buttonUp.png")
    private var buttons: List<Sprite> = listOf( Sprite(texture),Sprite(texture),
                                                Sprite(texture),Sprite(texture),
                                                Sprite(texture),Sprite(texture) )
    private var restart: Sprite = Sprite(Texture("Buttons/restart.png"))
    private var imgScale: Float = 0.8f
    private var rows: List<Float> = listOf(440f, 235f, 30f)
    private var columns: List<Float> = listOf(100f,315f)
    private var enable: MutableList<Float> = mutableListOf(1f,1f,1f,1f,1f,1f)
    private var interf: Restart = StartActivity()
    private var settings: Settings? = null
    ///other

    init{
        input = Input(this)
        settings = Settings(this)
        Gdx.input.inputProcessor = input
        restart.x = screenWidth * 15f / 16f
        restart.y = screenHeight * 7f / 8f
        restart.setScale(1.3f)
        restart.setBounds(restart.x, restart.y,restart.width,restart.height)
        for( i in buttons.indices step 2) {
            buttons[i].x = columns[1]
            buttons[i].y = rows[i/2]
            buttons[i+1].x = columns[0]
            buttons[i+1].y = rows[i/2]
            buttons[i].setScale(imgScale)
            buttons[i+1].setScale(imgScale)
            buttons[i].setBounds(buttons[i].x,buttons[i].y,buttons[i].width,buttons[i].height)
            buttons[i+1].setBounds(buttons[i+1].x,buttons[i+1].y,buttons[i+1].width,buttons[i+1].height)
            buttons[i+1].rotation = 180f
        }
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        for (i in buttons.indices)
            buttons[i].draw(batch,enable[i])
        restart.draw(batch)
        settings!!.draw(batch!!, parentAlpha)
    }

    override fun act(delta: Float) {
        when(ballon.getMass()){
            in 300.1f..999.9f -> { enable[0] = 1f; enable[1] = 1f}
            300f -> enable[1] = 0.5f
            1000f -> enable[0] = 0.5f
        }
        when(ballon.getVolume()){
            in 400.1f..4999.9f -> { enable[2] = 1f; enable[3] = 1f}
            400f -> enable[3] = 0.5f
            5000f -> enable[2] = 0.5f
        }
        when(ballon.getGasT()){
            in 35.1f..139.9f -> { enable[4] = 1f; enable[5] = 1f}
            35f -> enable[5] = 0.5f
            140f -> enable[4] = 0.5f
        }
    }

    fun check(x: Int, y: Int){
        when{
            ((x in buttons[0].x.toInt()..(buttons[0].x+buttons[0].width).toInt())&&(y in buttons[0].y.toInt()..(buttons[0].y+buttons[0].height).toInt())) -> ballon.massInc()
            ((x in buttons[1].x.toInt()..(buttons[1].x+buttons[1].width).toInt())&&(y in buttons[1].y.toInt()..(buttons[1].y+buttons[1].height).toInt())) -> ballon.massDec()
            ((x in buttons[2].x.toInt()..(buttons[2].x+buttons[2].width).toInt())&&(y in buttons[2].y.toInt()..(buttons[2].y+buttons[2].height).toInt())) -> ballon.volumeInc()
            ((x in buttons[3].x.toInt()..(buttons[3].x+buttons[3].width).toInt())&&(y in buttons[3].y.toInt()..(buttons[3].y+buttons[3].height).toInt())) -> ballon.volumeDec()
            ((x in buttons[4].x.toInt()..(buttons[4].x+buttons[4].width).toInt())&&(y in buttons[4].y.toInt()..(buttons[4].y+buttons[4].height).toInt())) -> ballon.tempInc()
            ((x in buttons[5].x.toInt()..(buttons[5].x+buttons[5].width).toInt())&&(y in buttons[5].y.toInt()..(buttons[5].y+buttons[5].height).toInt())) -> ballon.tempDec()
            ((x in restart.x.toInt()..(restart.x+restart.width).toInt())&&(y in restart.y.toInt()..(restart.y+restart.height).toInt())) -> restart(activity)
        }
        settings!!.check(x,y)
    }

    override fun restart(activity: StartActivity){
        interf.restart(activity)
    }
}