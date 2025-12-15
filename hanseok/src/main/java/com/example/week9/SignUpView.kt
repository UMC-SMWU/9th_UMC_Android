package com.example.week9

interface SignUpView {
    fun onSignUpSuccess()
    fun onSignUpFailure(code: String, message: String)

}