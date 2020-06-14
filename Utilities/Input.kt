package com.darius.luftballon.Utilities

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputProcessor
import com.darius.luftballon.Actors.Control


class Input(var control: Control) : InputProcessor
{
    private val screenHeight = Gdx.graphics.height
    private val screenWidth = Gdx.graphics.width //TODO DELETE

    init{

    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        control.check(screenX,screenHeight-screenY)
        return true
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return true
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        return true
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        return false // TODO USELESS
    }

    override fun keyTyped(character: Char): Boolean {
        return false // TODO USELESS
    }

    override fun scrolled(amount: Int): Boolean {
        return false // TODO USELESS
    }

    override fun keyUp(keycode: Int): Boolean {
        return false // TODO USELESS
    }

    override fun keyDown(keycode: Int): Boolean {
        return false // TODO USELESS
    }
}