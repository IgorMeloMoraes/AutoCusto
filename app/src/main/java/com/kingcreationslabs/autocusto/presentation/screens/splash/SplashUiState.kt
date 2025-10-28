package com.kingcreationslabs.autocusto.presentation.screens.splash

/**
 * Representa o estado da UI para a [SplashScreen].
 *
 * @property isLoading Indica se os dados das preferências ainda estão a ser carregados.
 * @property hasCompletedOnboarding Indica se o utilizador já completou o onboarding.
 */
data class SplashUiState(
    val isLoading: Boolean = true,
    val hasCompletedOnboarding: Boolean = false
)