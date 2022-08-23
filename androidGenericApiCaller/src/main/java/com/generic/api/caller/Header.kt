package com.generic.api.caller

object Header {
    const val SET_COOKIE = "set-cookie"
    const val COOKIE = "Cookie"
    var isHeader = true
    var token : String? = null
    var headers: HashMap<String,String> = HashMap()
}