package com.generic.api.caller


object RemoteDataSource {
    fun getData(url: String, vararg parameters: Any?):String{
        return if (parameters.isNotEmpty()) {
            if (parameters.hasData()) {
                val a = url + parameters.paramsConcatenate()
                a
            } else
                url
        }else
            url
    }
}





