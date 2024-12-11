package com.example.habittrackerapp

data class MenuItem(val iconId: Int? = null, val title: String, val onclick: () -> Unit)
