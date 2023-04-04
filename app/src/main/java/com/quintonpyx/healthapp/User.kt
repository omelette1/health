package com.quintonpyx.healthapp

class User {

    // val is immutable, var is mutable
    var name:String? = null
    var email:String? = null
    var uid:String? = null
    var photoUrl:String? = null
    var steps:Int? = null
    var targetSteps:Int? = null
    var targetCalorie:Int? = null

    constructor(){

    }

<<<<<<< HEAD
    constructor(name:String?,email:String?,uid:String?,photoUrl:String?,steps:Int = 0, targetSteps:Int = 0){
=======
    constructor(name:String?,email:String?,uid:String?,steps:Int = 0, targetSteps:Int = 0, targetCalorie:Int = 0){
>>>>>>> a4fa0919a9bafd165dad76e1222fe5a521db25ff
        this.name = name
        this.email = email
        this.uid = uid
        this.photoUrl = photoUrl
        this.steps = steps
        this.targetSteps = targetSteps
        this.targetCalorie = targetCalorie
    }
}