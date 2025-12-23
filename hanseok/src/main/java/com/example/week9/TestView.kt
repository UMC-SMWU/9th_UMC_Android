package com.example.week9

interface TestView {
    fun onTestSuccess(message: String)
    fun onTestFailure(code: Int, message: String)
}