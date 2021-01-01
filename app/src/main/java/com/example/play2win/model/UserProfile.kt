package com.example.play2win

data class UserProfile(
    val user_id: Int,
    val userName: String,
    val email: String,
    val mobile: String,
    val account: String,
    val publicKey: String,
    val privateKey: String,
    var balance : String
)