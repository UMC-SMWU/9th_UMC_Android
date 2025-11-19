package com.example.week9_10.ui.signup

//Activity와 AuthService 연결
interface SignUpView {
    fun onSignUpSuccess()
    fun onSignUpFailure()
}