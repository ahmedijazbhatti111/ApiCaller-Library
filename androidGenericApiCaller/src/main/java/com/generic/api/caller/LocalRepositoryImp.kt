package com.generic.api.caller

import android.content.SharedPreferences

class LocalRepositoryImp(private val sharedPreferences: SharedPreferences) : LocalRepository {
    override fun saveStringInPreferences(key: String, value: String) {
        sharedPreferences.edit().apply {
            putString(key, value)
        }.apply()
    }

    override fun getStringFromPreferences(key: String): String {
        return sharedPreferences.getString(key, null).toString()
    }
}