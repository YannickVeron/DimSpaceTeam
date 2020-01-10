package com.example.dimspaceteam

data class Action(
    val sentence: String,
    val uiElement: UIElement,
    val time: Long = 8000,
    var isDone: Boolean = false
)