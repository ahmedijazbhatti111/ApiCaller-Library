package com.generic.api.caller

interface LocalRepository {
    fun saveStringInPreferences(key:String,value:String)
    fun getStringFromPreferences(key:String):String
}