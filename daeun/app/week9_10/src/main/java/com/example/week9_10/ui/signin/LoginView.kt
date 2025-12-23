package com.example.week9_10.ui.signin

import com.example.week9_10.data.remote.Data

interface LoginView {
    fun onLoginSuccess(status:Boolean, data: Data)
    fun onLoginFailure()
}