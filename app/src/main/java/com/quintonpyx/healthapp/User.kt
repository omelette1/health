package com.quintonpyx.healthapp

class User {
    // val is immutable, var is mutable
    var name:String? = null
    var email:String? = null
    var uid:String? = null
    var photoUrl:String? = null
    var steps:Int? = null
    var targetSteps:Int? = null

    constructor(){

    }

    constructor(name:String?,email:String?,uid:String?,photoUrl:String?,steps:Int = 0, targetSteps:Int = 0){
        this.name = name
        this.email = email
        this.uid = uid
        this.photoUrl = photoUrl
        this.steps = steps
        this.targetSteps = targetSteps
    }
}