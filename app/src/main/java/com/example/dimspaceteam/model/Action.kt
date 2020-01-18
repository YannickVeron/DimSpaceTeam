package com.example.dimspaceteam.model

import com.example.dimspaceteam.model.UIElement

data class Action(
    val sentence: String,
    val uiElement: UIElement,
    val time: Long = 8000,
    var isDone: Boolean = false
)