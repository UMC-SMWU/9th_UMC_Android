package com.example.week9

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthService {
    private lateinit var signUpView: SignUpView
    private lateinit var loginView: LoginView

    private lateinit var testView: TestView

    fun setSignUpView(signUpView: SignUpView) {
        this.signUpView = signUpView
    }

    fun setLoginView(loginView: LoginView) {
        this.loginView = loginView
    }

    fun setTestView(testView: TestView) { // set 함수 추가
        this.testView = testView
    }

    fun signUp(user: User){

        val authService = getRetrofit().create(AuthRetrofitInterface::class.java)

        authService.signUp(user).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                Log.d("SIGNUP/SUCCESS", response.toString())
                if (response.isSuccessful && response.body() != null) {
                    val resp = response.body()!! // response가 null이 아님을 보장하기 때문에 !!를 사용했습니다.

                    when (resp.code) {
                        "COMMON201" -> signUpView.onSignUpSuccess()
                        else -> signUpView.onSignUpFailure(resp.code, resp.message)
                    }
                } else {
                    val errorCode = response.code().toString()
                    val errorMessage = "요청에 실패했습니다. ($errorCode)"
                    signUpView.onSignUpFailure(errorCode, errorMessage)
                }
            }
            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Log.d("SIGNUP/FAILURE", t.message.toString())
            }

        })

        Log.d("SIGNUP", "HELLO")
    }

    fun login(user: User) {
        val authService = getRetrofit().create(AuthRetrofitInterface::class.java)

        authService.login(user).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                Log.d("LOGIN/RESPONSE", response.toString())

                if (response.isSuccessful && response.body() != null) {
                    val resp = response.body()!!
                    when (resp.code) {
                        // 강제 언래핑 최소화 하는 방법이 더 있는지 모르겠네요
//                        "COMMON200_1" -> resp.data!!
                        "COMMON200_1" -> resp.data?.let { data ->
                            loginView.onLoginSuccess(data)
                        } ?: run {
                            loginView.onLoginFailure("DATA_ERROR", "응답 데이터가 null입니다.")
                        }
                        else -> loginView.onLoginFailure(resp.code, resp.message)
                    }
                } else {
                    val errorCode = response.code().toString()
                    loginView.onLoginFailure(errorCode, "로그인에 실패했습니다.")
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Log.d("LOGIN/FAILURE", t.message.toString())
                loginView.onLoginFailure("NetworkError", "네트워크 오류가 발생했습니다.")
            }
        })
    }


    fun testApi(accessToken: String) {
        val authService = getRetrofit().create(AuthRetrofitInterface::class.java)

        // API를 호출할 때 "Bearer " + 토큰 형식으로 전달
        authService.test("Bearer $accessToken").enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                if (response.isSuccessful) {
                    testView.onTestSuccess(response.body()!!.message)
                } else {
                    testView.onTestFailure(response.code(), "인증에 실패했습니다.")
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                testView.onTestFailure(0, "네트워크 오류")
            }
        })
    }

}