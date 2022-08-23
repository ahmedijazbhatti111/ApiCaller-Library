package com.practice.apicallingpractices.new_architecture_code.domain.model.user

data class User(
    val bio: String,
    val city: String,
    val country: String,
    val district: String,
    val firstName: String,
    val gender: String,
    val id: String,
    val language: String,
    val lastName: String,
    val middleName: String,
    val rating: Int,
    val role: String,
    val school: String,
    val teacherSettings: Any,
    val userName: String
)