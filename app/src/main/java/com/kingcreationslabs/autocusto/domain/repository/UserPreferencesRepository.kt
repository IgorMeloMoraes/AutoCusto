package com.kingcreationslabs.autocusto.domain.repository

import com.kingcreationslabs.autocusto.domain.model.AppTheme
import com.kingcreationslabs.autocusto.domain.model.UserPreferences
import kotlinx.coroutines.flow.Flow

/**
 * Interface que define o contrato para gerenciamento das preferências do usuário.
 * (Camada de Domínio)
 */
interface UserPreferencesRepository {

    /**
     * Um [Flow] que emite as [UserPreferences] atuais sempre que elas mudam.
     */
    val userPreferences: Flow<UserPreferences>

    /**
     * Salva o status de conclusão do onboarding.
     * @param completed O novo status (true se completou).
     */
    suspend fun setHasCompletedOnboarding(completed: Boolean)

    /**
     * Salva a nova preferência de tema do usuário.
     * @param theme O novo [AppTheme] escolhido.
     */
    suspend fun setAppTheme(theme: AppTheme)
}