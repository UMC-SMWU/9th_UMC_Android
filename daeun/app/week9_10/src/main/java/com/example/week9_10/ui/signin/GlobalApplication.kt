package com.example.week9_10.ui.signin

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.example.week9_10.BuildConfig

class GlobalApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, BuildConfig.KAKAO_API_KEY)
    }
}