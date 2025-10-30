plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)

    alias(libs.plugins.ksp)           // Para Hilt e Room
    alias(libs.plugins.hilt)          // Para Injeção de Dependência
    alias(libs.plugins.googleServices)  // Para o Firebase
    alias(libs.plugins.dokka) // Para o Dokka
}

android {
    namespace = "com.kingcreationslabs.autocusto"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.kingcreationslabs.autocusto"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    // Habilita Java 17
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    // Habilita Compose
    buildFeatures {
        compose = true
    }
    // Define a versão do compilador (agora lendo do .toml)
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.kotlinCompilerExtension.get()
    }
}

dependencies {

    // --- CORE E LIFECYCLE ---
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)

    // --- JETPACK COMPOSE (BOM) ---
    implementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(platform(libs.androidx.compose.bom))

    // Bibliotecas do Compose
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)

    // --- NAVEGAÇÃO & VIEWMODEL (COMPOSE) ---
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // --- FIREBASE (BOM) ---
    val firebaseBom = platform(libs.firebase.bom)
    implementation(firebaseBom)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)

    // --- HILT (DI) ---
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler) // KSP ao invés de KAPT

    // --- ROOM (DATABASE) ---
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler) // KSP ao invés de KAPT

    // --- DATASTORE (SETTINGS) ---
    implementation(libs.androidx.datastore.preferences)

    // --- TESTES ---
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.androidx.navigation.testing)

// --- DEBUG ---
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // Facebook SDK
    implementation(libs.facebook.login)

}