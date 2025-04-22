plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.strip"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.strip"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    }
    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        debug {
            buildConfigField("String", "API_URL", "\"${project.properties["API_URL"]}\"")
            buildConfigField("String", "SERVER_HOST", "\"${project.properties["SERVER_HOST"]}\"")
            buildConfigField("String", "MOBILE_HOST", "\"${project.properties["MOBILE_HOST"]}\"")
        }
        release {
            buildConfigField("String", "API_URL", "\"${project.properties["API_URL"]}\"")
            buildConfigField("String", "SERVER_HOST", "\"${project.properties["SERVER_HOST"]}\"")
            buildConfigField("String", "MOBILE_HOST", "\"${project.properties["MOBILE_HOST"]}\"")
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.play.services.fitness)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")

    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.2")
    implementation ("org.json:json:20210307")

    implementation ("com.auth0:java-jwt:3.18.2")

    // ViewPager2 dependency
    implementation ("androidx.viewpager2:viewpager2:1.0.0")
    // Rounded ImageView dependency
    implementation ("com.makeramen:roundedimageview:2.3.0")
    implementation ("com.google.android.material:material:1.11.0")

    //chat
    implementation ("im.crisp:crisp-sdk:2.0.5")
    implementation ("androidx.multidex:multidex:2.0.1")
    //map
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation("com.google.maps.android:android-maps-utils:2.2.3")
    implementation("com.squareup.picasso:picasso:2.8")
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation("androidx.cardview:cardview:1.0.0")

    implementation("org.osmdroid:osmdroid-android:6.1.11")


    // to offer PayPal
    implementation("com.braintreepayments.api:paypal:4.38.0")

    // to offer local payments
    implementation("com.braintreepayments.api:drop-in:6.16.0")
    implementation("com.braintreepayments.api:card:4.38.0")
    implementation("androidx.activity:activity:1.7.2")

}