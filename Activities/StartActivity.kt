package com.darius.luftballon.Activities

import com.badlogic.gdx.Game
import com.darius.luftballon.Utilities.Restart

class StartActivity : Game(), Restart {

    var simulation : Simulation? = null

    override fun create() {
        simulation = Simulation(this)
        setScreen(simulation)
    }

    override fun dispose() {

    }

    override fun restart(activity: StartActivity) {
        println("RESTART")
        activity.simulation!!.dispose()
        activity.simulation = Simulation(activity)
        activity.setScreen(activity.simulation)
    }
}