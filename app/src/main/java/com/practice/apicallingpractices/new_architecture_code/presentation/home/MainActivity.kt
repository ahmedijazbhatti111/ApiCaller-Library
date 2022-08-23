package com.practice.apicallingpractices.new_architecture_code.presentation.home

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.generic.api.caller.*
import com.practice.apicallingpractices.R
import com.practice.apicallingpractices.new_architecture_code.commons.constants.ApiPath
import com.practice.apicallingpractices.new_architecture_code.domain.model.Response


class MainActivity : AppCompatActivity() {


    private val apiCallerViewModel: ApiCallerViewModel by lazy {
        ViewModelProvider(this,ApiCallerViewModelProvider(this))[ApiCallerViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ApiCaller.config("https://staging.tabsera.com",
            "Content-Type" to "application/json")

        val login = findViewById<Button>(R.id.login)
        val call = findViewById<Button>(R.id.call)
        val text = findViewById<TextView>(R.id.text1)
        val text2 = findViewById<TextView>(R.id.text2)

        login.setOnClickListener {
            apiCallerViewModel.callApiForResponse(
                ApiMethod.POST_BODY,
                ApiPath.LOGIN_REQUEST,
                "userNameOrPhone" to "osamakhanedu",
                "password" to "test@123",
                tokenKey = Header.SET_COOKIE
            ).collectValue<Response>(this, isDefaultLoader = true) {
                text.text = it.user?.firstName + " " + it.user?.lastName + " " + it.user?.city
                Toast.makeText(this, "LOGIN_REQUEST call", Toast.LENGTH_SHORT).show()
            }
        }

        call.setOnClickListener {
            apiCallerViewModel.callApiForResponse(
                ApiMethod.GET,
                ApiPath.GET_COURSES,
                "mode" to "live",
                "offset" to "5",
                "limit" to "100", isCache = true
            ).collectValue<Response>(this, isDefaultLoader = true) {
                if (it.past?.count != null) {
                    text2.text = it.past.count.toString()
                    Toast.makeText(this, "courxse call", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}