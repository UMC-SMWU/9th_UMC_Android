package com.example.week9_10.data.remote

import android.util.Log
import com.example.week9_10.ui.signin.LoginView
import com.example.week9_10.ui.signup.SignUpView
import com.example.week9_10.data.entities.User
import com.example.week9_10.data.entities.UserUpdate
import com.example.week9_10.utils.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthService {
    private lateinit var signUpView: SignUpView
    private lateinit var loginView: LoginView

    fun setSignUpView(signUpView: SignUpView){
        this.signUpView = signUpView
    }
    fun setLoginView(loginView: LoginView){
        this.loginView = loginView
    }
    fun signUp(user: User){
        val authService = getRetrofit().create(AuthRetrofitInterface::class.java)
        authService.signUp(user).enqueue(object: Callback<AuthResponse> {
            override fun onFailure(call: Call<AuthResponse?>, t: Throwable) {
                Log.d("SignUp/Failure",t.message.toString())
            }
            override fun onResponse(call: Call<AuthResponse?>, response: Response<AuthResponse?>) {
                Log.d("SignUp/Success",response.toString())
                val resp: AuthResponse = response.body() ?: AuthResponse(false,"","",null)
                when(resp.status){
                    true -> signUpView.onSignUpFailure()
                    false -> signUpView.onSignUpFailure()
               }
            }
        })
    }

    fun login(user: User){
        val authService = getRetrofit().create(AuthRetrofitInterface::class.java)
        authService.login(user).enqueue(object: Callback<AuthResponse> {
            override fun onFailure(call: Call<AuthResponse?>, t: Throwable) {
                Log.d("Login/Failure",t.message.toString())
            }
            override fun onResponse(call: Call<AuthResponse?>, response: Response<AuthResponse?>) {
                Log.d("Login/Success",response.toString())
                val resp: AuthResponse = response.body() ?: AuthResponse(false,"","",null)
                when(val status = resp.status){
                    true -> loginView.onLoginSuccess(status,resp.data!!)
                    else -> loginView.onLoginFailure()
                }
            }
        })
    }

    fun test(token: String){
        val bearerToken = "Bearer $token"
        val authService = getRetrofit().create(AuthRetrofitInterface::class.java)
        authService.test(bearerToken).enqueue(object: Callback<AuthResponse>{
            override fun onResponse(
                call: Call<AuthResponse?>,
                response: Response<AuthResponse?>
            ) {
                if(response.isSuccessful){
                    val resp: AuthResponse = response.body() ?: AuthResponse(false,"","",null)
                    if(resp.status)
                        Log.d("Test/Success", resp.message)
                }
                else{
                    // response code: 400, 500
                    try {
                        val errorBody = response.errorBody()?.string()
                        Log.d("Test/Failure", "HTTP Error Code: ${response.code()}, Error Body: $errorBody")
                    } catch (e: Exception) {
                        Log.e("Test/Failure", "HTTP Error Code: ${response.code()}, Could not parse error body: ${e.message}")
                    }
                }

            }

            override fun onFailure(
                call: Call<AuthResponse?>,
                t: Throwable
            ) {
                Log.d("Test/Failure",t.message.toString())
            }

        })
    }
    fun update(token: String, user: UserUpdate){
        val bearerToken = "Bearer $token"
        val authService = getRetrofit().create(AuthRetrofitInterface::class.java)
        authService.update(bearerToken, user).enqueue(object:Callback<AuthResponse>{
            override fun onResponse(
                call: Call<AuthResponse?>,
                response: Response<AuthResponse?>
            ) {
                if(response.isSuccessful){
                    val resp: AuthResponse = response.body() ?: AuthResponse(false,"","",null)
                    if(resp.status)
                        Log.d("Update/Success", resp.message)
                }
                else{
                    // response code: 400, 500
                    try {
                        val errorBody = response.errorBody()?.string()
                        Log.d("Update/Failure", "HTTP Error Code: ${response.code()}, Error Body: $errorBody")
                    } catch (e: Exception) {
                        Log.e("Update/Failure", "HTTP Error Code: ${response.code()}, Could not parse error body: ${e.message}")
                    }
                }
            }

            override fun onFailure(
                call: Call<AuthResponse?>,
                t: Throwable
            ) {
                Log.d("Update/Failure",t.message.toString())
            }

        })

    }

    fun delete(token:String, memberId: Int, password: String){
        val bearerToken = "Bearer $token"
        val authService = getRetrofit().create(AuthRetrofitInterface::class.java)
        authService.delete(bearerToken, memberId, password).enqueue(object:Callback<AuthResponse>{
            override fun onResponse(
                call: Call<AuthResponse?>,
                response: Response<AuthResponse?>
            ) {
                if(response.isSuccessful){
                    val resp: AuthResponse = response.body() ?: AuthResponse(false,"","",null)
                    if(resp.status)
                        Log.d("Delete/Success", resp.message)
                }
                else{
                    // response code: 400, 500
                    try {
                        val errorBody = response.errorBody()?.string()
                        Log.d("Delete/Failure", "HTTP Error Code: ${response.code()}, Error Body: $errorBody")
                    } catch (e: Exception) {
                        Log.e("Delete/Failure", "HTTP Error Code: ${response.code()}, Could not parse error body: ${e.message}")
                    }
                }
            }

            override fun onFailure(
                call: Call<AuthResponse?>,
                t: Throwable
            ) {
                Log.d("Delete/Failure",t.message.toString())
            }

        })
    }
}