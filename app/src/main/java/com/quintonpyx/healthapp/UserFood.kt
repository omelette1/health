package com.quintonpyx.healthapp

class UserFood {
    // val is immutable, var is mutable
    var uid:String? = null
    var user:String? = null
    var food:String? = null
    var calorie:Int? = null
    var date:String? = null
    constructor(){

    }

    constructor(uid:String?,user:String?,food:String?,calorie:Int?,date:String?){
        this.uid = uid
        this.user = user
        this.food = food
        this.calorie = calorie
        this.date = date
    }
}