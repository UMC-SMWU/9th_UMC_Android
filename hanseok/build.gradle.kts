import java.nio.charset.Charset
import java.util.Properties

    plugins {
        alias(libs.plugins.android.application)
        alias(libs.plugins.kotlin.android)
        alias(libs.plugins.google.gms.google.services)
    }

    val localProperties = Properties().apply {  // 🔽 java.util. 제거
        val localPropertiesFile = rootProject.file("local.properties")
        if (localPropertiesFile.exists()) {
            load(localPropertiesFile.reader(Charset.forName("UTF-8")))
        }
    }

    android {
        namespace = "com.example.week9"
        compileSdk = 36
        buildFeatures {
            viewBinding = true
            buildConfig = true
        }
        defaultConfig {
            applicationId = "com.example.week9"
            minSdk = 33
            targetSdk = 36
            versionCode = 1
            versionName = "1.0"

            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            manifestPlaceholders["KAKAO_API_KEY"] = "\"${localProperties["kakao_native_app_key"] ?: ""}\""
            buildConfigField("String", "KAKAO_NATIVE_APP_KEY", "\"${localProperties["kakao_native_app_key"]}\"")
        }

        buildTypes {
            release {
                isMinifyEnabled = false
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            }
        }
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
        }
        kotlinOptions {
            jvmTarget = "11"
        }
    }

    dependencies {

        implementation("com.google.firebase:firebase-auth-ktx:23.0.0")
        implementation("com.google.firebase:firebase-database-ktx:21.0.0")

        implementation(libs.androidx.core.ktx)
        implementation(libs.androidx.appcompat)
        implementation(libs.material)
        implementation(libs.androidx.activity)
        implementation(libs.androidx.constraintlayout)
        implementation("com.google.code.gson:gson:2.10.1")
        implementation("me.relex:circleindicator:2.1.6")
        implementation(libs.google.firebase.database)


        testImplementation(libs.junit)
        androidTestImplementation(libs.androidx.junit)
        androidTestImplementation(libs.androidx.espresso.core)

        implementation("com.squareup.retrofit2:retrofit:2.9.0")
        implementation("com.squareup.retrofit2:converter-gson:2.9.0")
        implementation("com.squareup.retrofit2:adapter-rxjava2:2.9.0")

        implementation("com.squareup.okhttp3:okhttp:4.9.0")
        implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")

        implementation("com.github.bumptech.glide:glide:4.11.0")
        annotationProcessor("com.github.bumptech.glide:compiler:4.11.0")

        implementation("com.kakao.sdk:v2-user:2.13.0")
        implementation("com.kakao.sdk:v2-auth:2.13.0")
    }

    apply(plugin = "com.google.gms.google-services")