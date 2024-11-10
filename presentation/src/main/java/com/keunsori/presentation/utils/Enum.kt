package com.keunsori.presentation.utils

enum class Navigation(val route: String) {
    Login("login_screen"), Main("main_screen"), Game("game_screen/{level}"), Info("info_screen"),
    Main_ChooseLevel("${Main.route}/choose-level")
}