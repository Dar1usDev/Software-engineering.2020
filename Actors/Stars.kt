package com.darius.luftballon.Actors

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.scenes.scene2d.Actor
import com.darius.luftballon.Activities.Simulation
import kotlin.random.Random

class Stars (private var screen: Simulation): Actor() {
    private val screenHeight = Gdx.graphics.height
    private val screenWidth = Gdx.graphics.width
    private var stars: List<Sprite> = listOf(Sprite(Texture("Stars/zvezdochka1.png")),Sprite(Texture("Stars/zvezdochka1.png")),
            Sprite(Texture("Stars/zvezdochka2.png")),Sprite(Texture("Stars/zvezdochka3.png"))
            ,Sprite(Texture("Stars/zvezdochka3.png")),Sprite(Texture("Stars/zvezdochka4.png")))
    private var alpha: MutableList<Float> = mutableListOf(0f,0f,0f,0f,0f,0f)
    private var rising: MutableList<Boolean> = mutableListOf(true,true,true,true,true,true)
    private var shining: MutableList<Boolean> = mutableListOf(false,false,false,false,false,false)
    private var alphaStep: Float = 0.0075f

    init{
        for( i in stars.indices)
            stars[i].setScale(0.3f)
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        for( i in stars.indices)
            if(shining[i])
                stars[i].draw(batch,alpha[i])
    }

    override fun act(delta: Float) {
        for( i in stars.indices) {
            if (!shining[i] && Random.nextDouble() > 0.99 && !screen.day) {
                shining[i] = true
                rising[i] = true
                stars[i].x = Random.nextDouble(0.0,screenWidth.toDouble()- stars[i].texture!!.width).toFloat()
                stars[i].y = Random.nextDouble(0.0,screenHeight.toDouble()- stars[i].texture!!.height).toFloat()
            }
            if(!shining[i]) continue
            when(rising[i]){
                true ->  {alpha[i]+= alphaStep}
                false -> {alpha[i]-= alphaStep}
            }
            if(alpha[i]>=1) {alpha[i] = 1f; rising[i] = false }
            if(alpha[i]<=0) {alpha[i] = 0f; shining[i] = false }
        }
    }
}