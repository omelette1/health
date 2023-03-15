package com.quintonpyx.healthapp

class Food {
    // val is immutable, var is mutable
    var name:String? = null
    var calorie:Int? = null

    constructor(){

    }

    constructor(name:String?,calorie:Int?){
        this.name = name
        this.calorie = calorie

    }
}