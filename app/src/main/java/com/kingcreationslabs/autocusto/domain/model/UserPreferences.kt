package com.kingcreationslabs.autocusto.domain.model

/**
 * Data class que encapsula todas as preferências do usuário.
 *
 * @property hasCompletedOnboarding Indica se o usuário já passou pelo carrossel de onboarding.
 * @property appTheme A preferência de tema (Claro, Escuro ou Padrão do Sistema).
 */
data class UserPreferences(
    val hasCompletedOnboarding: Boolean,
    val appTheme: AppTheme
)