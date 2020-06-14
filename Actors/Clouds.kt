package com.darius.luftballon.Actors

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.scenes.scene2d.Actor
import kotlin.random.Random

class Clouds(far_: Boolean,var ballon: Ballon): Actor() {

    private val screenHeight = Gdx.graphics.height
    private val screenWidth = Gdx.graphics.width
    private var clouds: List<Sprite> = listOf(Sprite())
    private var exist: MutableList<Boolean> = mutableListOf(false,false,false,false)
    private var far: Boolean? = null
    private var ballspeed: Float? = null

    init{
        this.far = far_
        this.clouds = listOf(Sprite(Texture("Clouds/oblachko1.png")), Sprite(Texture("Clouds/oblachko2.png"))
                , Sprite(Texture("Clouds/oblachko3.png")), Sprite(Texture("Clouds/oblachko4.png")) )
        for(i in this.clouds.indices)
            when(this.far!!){
                true -> { this.clouds[i].setScale(0.2f)}
                false -> { this.clouds[i].setScale(0.5f)}
            }
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        for(i in this.clouds.indices)
            if(this.exist[i]) this.clouds[i].draw(batch)
    }

    override fun act(delta: Float) {
        for(i in clouds.indices) {
            ballspeed = ballon.getSpeed()
            if(!exist[i] && Random.nextDouble() > 0.9985 && (far!! || !far!! && ballon.getFlightHeight() > 200)) {
                exist[i] = true
                if(ballspeed!! in -0.2f..0.2f ){
                    clouds[i].x = screenWidth + 1f
                    clouds[i].y = Random.nextDouble(clouds[i].width.toDouble(), screenHeight.toDouble()-clouds[i].height).toFloat()
                }else {
                    if (ballspeed!! < 0)
                        clouds[i].y = 0f - 1f - clouds[i].height
                     else
                        clouds[i].y = screenHeight + 1f
                    clouds[i].x = Random.nextDouble(clouds[i].width.toDouble(), screenWidth.toDouble()).toFloat()
                }
            }
            if(!exist[i])
                continue
            when(far!!){
                true -> clouds[i].x -= 0.7f
                false -> clouds[i].x -= 2f
            }
            when(far!!){
                true -> clouds[i].y -= ballon.getSpeed()
                false -> clouds[i].y -= ballon.getSpeed() * 4f
            }
            if(clouds[i].y > screenHeight + 2f || clouds[i].y < (0 - clouds[i].height - 2f) || clouds[i].x < 0 - clouds[i].width - 2f  )
                exist[i] = false
        }
    }
}