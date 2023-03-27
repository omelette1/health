package com.quintonpyx.healthapp

class UserFood {
    // val is immutable, var is mutable
    var uid:String? = null
    var food:String? = null
    var calorie:Int? = null


    constructor(){

    }

    constructor(uid:String?,food:String?,calorie:Int?){
        this.uid = uid
        this.food = food
        this.calorie = calorie
    }
}