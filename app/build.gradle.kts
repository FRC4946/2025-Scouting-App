plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.a2024scoutingapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.a2024scoutingapp"
        minSdk = 26
        //noinspection ExpiredTargetSdkVersion
        targetSdk = 30
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
    packagingOptions {
        resources {
            excludes += "/META-INF/DEPENDENCIES" // help kill me
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation("com.google.guava:listenablefuture:9999.0-empty-to-avoid-conflict-with-guava")
    implementation ("pl.droidsonroids.gif:android-gif-drawable:1.2.25")
    implementation("com.google.android.material:material:1.11.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation ("com.android.volley:volley:1.2.1")
    implementation ("com.google.api-client:google-api-client:2.0.0")
    implementation ("com.google.oauth-client:google-oauth-client-jetty:1.34.1")
    implementation ("pub.devrel:easypermissions:3.0.0")
    implementation ("com.google.http-client:google-http-client-android:1.38.1")
    implementation("com.google.android.material:material:1.11.0")
    testImplementation("junit:junit:4.13.2")
    implementation ("com.android.volley:volley:1.2.1")
    implementation ("com.google.api-client:google-api-client:1.30.4")
    implementation ("com.google.oauth-client:google-oauth-client-jetty:1.30.4")
    implementation ("com.google.apis:google-api-services-sheets:v4-rev581-1.25.0")
    implementation ("com.google.api-client:google-api-client:1.30.4")
    implementation ("com.google.api-client:google-api-client-android:1.30.4")
    implementation ("com.google.http-client:google-http-client-gson:1.30.4")
    implementation ("com.google.android.gms:play-services-auth:20.7.0")
    implementation("com.google.api-client:google-api-client-android:1.23.0")
    implementation ("com.google.apis:google-api-services-sheets:v4-rev20220927-2.0.0")
    //implementation ("com.android.support:appcompat-v7:26.1.0")
    //implementation ("com.android.support:support-v4:26.1.0")
    //implementation ("com.android.support:support-fragment:26.1.0")
    //implementation ("com.android.support:design:26.1.0")
    //implementation ("com.android.support.constraint:constraint-layout:1.0.2")
}