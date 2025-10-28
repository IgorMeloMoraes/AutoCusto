package com.kingcreationslabs.autocusto.presentation.screens.onboarding

import androidx.annotation.DrawableRes

/**
 * Modelo de dados para representar uma única página
 * no carrossel de onboarding.
 *
 * @property imageRes O ID do recurso (drawable) da imagem.
 * @property title O título principal da página.
 * @property description A descrição da página.
 */
data class OnboardingPage(
    @DrawableRes val imageRes: Int,
    val title: String,
    val description: String
)