package com.darius.luftballon.Utilities

import com.darius.luftballon.Activities.StartActivity

interface SPenInput {
    fun action(type: Int){
        println(" $type")
    }
}