package com.quintonpyx.healthapp

import com.quintonpyx.healthapp.helper.GeneralHelper

class User {

    // val is immutable, var is mutable
    var name:String? = null
    var email:String? = null
    var uid:String? = null
    var photoUrl:String? = null
    var steps:Int? = null
    var targetSteps:Int? = null
    var targetCalorie:Int? = null
    var yesterdaySteps:Int? = null
    var lastUpdated:String? = null

    constructor(){

    }

    constructor(name:String?,email:String?,uid:String?,photoUrl:String?,steps:Int = 0, targetSteps:Int = 1000, targetCalorie:Int = 2000, yesterdaySteps:Int=0, lastUpdated:String=GeneralHelper.getTodayDate()){
        this.name = name
        this.email = email
        this.uid = uid
        this.photoUrl = photoUrl
        this.steps = steps
        this.targetSteps = targetSteps
        this.targetCalorie = targetCalorie
        this.yesterdaySteps = yesterdaySteps
        this.lastUpdated = lastUpdated
    }
}