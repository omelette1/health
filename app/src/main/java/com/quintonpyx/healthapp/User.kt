package com.quintonpyx.healthapp

class User {
    // val is immutable, var is mutable
    var name:String? = null
    var email:String? = null
    var uid:String? = null
    var steps:Int? = null

    constructor(){

    }

    constructor(name:String?,email:String?,uid:String?,steps:Int?){
        this.name = name
        this.email = email
        this.uid = uid
        this.steps = steps
    }
}