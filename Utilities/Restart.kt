package com.darius.luftballon.Utilities

import com.darius.luftballon.Activities.StartActivity

interface Restart {
    fun restart(activity: StartActivity){
        println("INTERFACE")
    }
}