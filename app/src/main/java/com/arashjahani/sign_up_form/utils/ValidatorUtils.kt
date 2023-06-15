package com.arashjahani.sign_up_form.utils

fun String.isEmailValid(): Boolean {
    val emailRegex = Regex("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,3})+$")
    return this.matches(emailRegex)
}