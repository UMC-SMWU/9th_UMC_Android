package com.example.week9

interface LoginView {
    fun onLoginSuccess(result: AuthResult)
    fun onLoginFailure(code: String, message: String)
}
