package com.example.week9

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.example.week9.BuildConfig
class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)
    }
}