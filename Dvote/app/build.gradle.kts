plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.dvote"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.dvote"
        minSdk = 21
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("com.tbuonomo:dotsindicator:4.3")
    implementation("com.google.code.gson:gson:2.8.9")
    implementation ("com.squareup.retrofit2:converter-gson:2.7.2")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation("com.owlike:genson:1.5")
    implementation ("com.getkeepsafe.taptargetview:taptargetview:1.13.3")
    implementation ("org.hyperledger.fabric:fabric-gateway:1.4.0") {
        exclude(group = "com.google.api.grpc", module = "proto-google-common-protos")
    }
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation ("com.google.android.material:material:1.8.0>")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    //implementation("com.google.api-client:google-api-client-android:1.32.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}