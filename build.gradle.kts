// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false

    // Plugin KSP (substituto mais rápido do kapt) - Vamos usar para Room e Hilt
    alias(libs.plugins.ksp) apply false

    // Plugin do Hilt para Injeção de Dependência
    alias(libs.plugins.hilt) apply false

    // Plugin do Google Services para o Firebase
    alias(libs.plugins.googleServices) apply false

    // Aplicamos o Dokka
    alias(libs.plugins.dokka) apply false
}