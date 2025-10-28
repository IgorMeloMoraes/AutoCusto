package com.kingcreationslabs.autocusto.domain.repository

import com.kingcreationslabs.autocusto.domain.model.AppTheme
import com.kingcreationslabs.autocusto.domain.model.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Implementação falsa (fake) do [UserPreferencesRepository] para uso em testes de unidade.
 *
 * Permite-nos controlar diretamente o estado das preferências que o ViewModel irá ler.
 */
class FakeUserPreferencesRepository : UserPreferencesRepository {

    // O estado "falso" que podemos controlar
    private val _preferences = MutableStateFlow(
        UserPreferences(
            hasCompletedOnboarding = false, // Valor padrão de teste
            appTheme = AppTheme.SYSTEM_DEFAULT
        )
    )

    override val userPreferences: Flow<UserPreferences> = _preferences.asStateFlow()

    // Métodos de salvamento (não precisamos deles para este teste, mas implementamos)
    override suspend fun setHasCompletedOnboarding(completed: Boolean) {
        _preferences.update { it.copy(hasCompletedOnboarding = completed) }
    }

    override suspend fun setAppTheme(theme: AppTheme) {
        _preferences.update { it.copy(appTheme = theme) }
    }

    // --- Método de Controle do Teste ---

    /**
     * Método utilitário para o teste definir o estado inicial
     * ANTES que o ViewModel seja criado.
     */
    fun setInitialOnboardingStatus(completed: Boolean) {
        _preferences.update { it.copy(hasCompletedOnboarding = completed) }
    }
}