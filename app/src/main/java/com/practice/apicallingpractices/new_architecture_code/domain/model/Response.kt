package com.practice.apicallingpractices.new_architecture_code.domain.model

import com.practice.apicallingpractices.new_architecture_code.domain.model.courses.Active
import com.practice.apicallingpractices.new_architecture_code.domain.model.courses.Past
import com.practice.apicallingpractices.new_architecture_code.domain.model.courses.Upcoming
import com.practice.apicallingpractices.new_architecture_code.domain.model.user.User


data class Response(
    val userNameOrPhone: String? = null,
    val password: String? = null,
    val user: User? = null,
    val wallet: String? = null,
    val message: String? = null,
    val active: Active?=null,
    val past: Past?=null,
    val upcoming: Upcoming?=null,
    val fact: String,
    val length: Int,
    val age: Int,
    val count: Int,
    val name: String,
    var apiUrl: String,
)