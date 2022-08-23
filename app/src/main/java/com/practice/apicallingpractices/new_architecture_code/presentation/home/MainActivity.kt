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
        ViewModelProvider(this, ApiCallerViewModelProvider(this))[ApiCallerViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ApiCaller.config("https://api.agify.io")

        val call = findViewById<Button>(R.id.call).also {
            it.text = "Call https://api.agify.io/?name=meelad"
        }
        val text = findViewById<TextView>(R.id.text1)

        call.setOnClickListener {
            apiCallerViewModel.callApiForResponse(
                ApiMethod.GET,
                "", "name" to "meelad",
                isCache = false
            ).collectValue<Response>(this, isDefaultLoader = true) {
                text.text = "name: " + it.name + ", age: " + it.age
            }
        }
    }
}