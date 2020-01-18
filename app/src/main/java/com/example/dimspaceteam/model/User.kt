package com.example.dimspaceteam.model

enum class State(val value: Int) {
    WAITING(0), READY(1), IN_GAME(2), OVER(3)
}

data class User(val id: Int, val name: String, val avatar: String, var score: Int, var state: State = State.OVER)

data class UserPost(val name: String)