package com.kingcreationslabs.autocusto.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.kingcreationslabs.autocusto.domain.model.AppTheme
import com.kingcreationslabs.autocusto.domain.model.UserPreferences
import com.kingcreationslabs.autocusto.domain.repository.UserPreferencesRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

// Define a instância do DataStore (nível de App)
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "autocusto_user_prefs")

/**
 * Implementação concreta do [UserPreferencesRepository] que usa o Jetpack DataStore.
 * (Camada de Dados)
 *
 * @param context O contexto da aplicação, injetado pelo Hilt.
 */
class UserPreferencesRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : UserPreferencesRepository {

    // Chaves de acesso para as preferências
    private object PreferencesKeys {
        val HAS_COMPLETED_ONBOARDING = booleanPreferencesKey("has_completed_onboarding")
        val APP_THEME = stringPreferencesKey("app_theme")
    }

    // DataStore
    private val dataStore = context.dataStore

    override val userPreferences: Flow<UserPreferences> = dataStore.data
        .catch { exception ->
            // Em caso de erro de leitura (ex: permissão), emite preferências vazias
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            // Mapeia as preferências brutas para o nosso modelo de domínio
            val hasCompletedOnboarding = preferences[PreferencesKeys.HAS_COMPLETED_ONBOARDING] ?: false

            // Mapeia a String salva de volta para o Enum
            val appTheme = AppTheme.valueOf(
                preferences[PreferencesKeys.APP_THEME] ?: AppTheme.SYSTEM_DEFAULT.name
            )

            UserPreferences(hasCompletedOnboarding, appTheme)
        }

    override suspend fun setHasCompletedOnboarding(completed: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.HAS_COMPLETED_ONBOARDING] = completed
        }
    }

    override suspend fun setAppTheme(theme: AppTheme) {
        dataStore.edit { preferences ->
            // Salva o nome do Enum (ex: "DARK") como String
            preferences[PreferencesKeys.APP_THEME] = theme.name
        }
    }
}