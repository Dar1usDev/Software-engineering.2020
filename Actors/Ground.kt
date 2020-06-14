package com.darius.luftballon.Actors

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.scenes.scene2d.Actor
import kotlin.random.Random

class Ground(var ballon: Ballon): Actor() {

    private val screenHeight = Gdx.graphics.height
    private val screenWidth = Gdx.graphics.width

    private val treeimg = Texture("derevtse.png")
    private val grassimg = Texture("grass.png")
    private val treescale = 0.3f
    private val grassscale = 0.2f
    private val yscale = 0.8f
    private val groundstep = 5f
    private var treesy = -290f - ballon.getFlightHeight() * yscale
    private var grassy = -110f - ballon.getFlightHeight() * yscale
    private var trees: MutableList<Sprite> = mutableListOf()
    private var ground: MutableList<Sprite> = mutableListOf()

    init{
        var leftx: Float = -420f
        while(leftx < screenWidth.toFloat() + grassimg.width * grassscale * 2){
            var grass = Sprite(grassimg)
            grass.y = -110f
            grass.x = leftx
            grass.setScale(grassscale)
            ground.add(grass)
            leftx += grass.width * grassscale
        }
        var newtree = Sprite(treeimg)
        newtree.x = Random.nextDouble(screenWidth.toDouble()/4,screenWidth.toDouble()).toFloat()
        newtree.y = -290f
        newtree.setScale(treescale)
        trees.add(newtree)
        newtree = Sprite(treeimg)
        newtree.x = Random.nextDouble(screenWidth.toDouble()).toFloat()
        newtree.y = -290f
        newtree.setScale(treescale)
        trees.add(newtree)
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        if (ballon.getFlightHeight() > 400) return
        for( i in trees)
            i.draw(batch)
        for( i in ground)
            i.draw(batch)
    }

    override fun act(delta: Float) {
        treesy = -290f - ballon.getFlightHeight() * yscale
        grassy = -110f - ballon.getFlightHeight() * yscale

        if (ballon.getFlightHeight() > 400f) return

        for (i in trees.size - 1 downTo 0)
            if (trees[i].x < -treeimg.width * 1.5f)
                trees.removeAt(i)
        if (Random.nextDouble() > 0.99) {
            val newtree = Sprite(treeimg)
            newtree.x = screenWidth.toFloat() + Random.nextDouble(screenWidth.toDouble() / 2.0).toFloat()
            newtree.setScale(treescale)
            trees.add(newtree)
        }
        if (ground[0].x < -grassimg.width.toFloat() * grassscale - 420f) {
            ground.removeAt(0)
            val grass = Sprite(grassimg)
            grass.x = ground[ground.size - 1].x + grassimg.width * grassscale
            grass.setScale(grassscale)
            ground.add(grass)
        }

        for (i in trees.indices)
            trees[i].y = treesy
        for (i in ground.indices)
            ground[i].y = grassy

        if(ballon.getFlightHeight() < 2f) return
        for (i in trees.indices)
            trees[i].x -= groundstep
        for (i in ground.indices)
            ground[i].x -= groundstep
    }
}