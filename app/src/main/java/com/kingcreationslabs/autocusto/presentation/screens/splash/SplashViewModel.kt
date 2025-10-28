package com.kingcreationslabs.autocusto.presentation.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kingcreationslabs.autocusto.domain.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para a [SplashScreen].
 *
 * Este ViewModel é responsável por verificar o [UserPreferencesRepository]
 * para determinar se o utilizador já completou o onboarding.
 *
 * @property userPreferencesRepository O repositório para aceder às preferências do utilizador.
 */
@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    // Estado interno mutável da UI
    private val _uiState = MutableStateFlow(SplashUiState())

    // Estado público e imutável da UI, exposto para o Composable
    val uiState: StateFlow<SplashUiState> = _uiState.asStateFlow()

    /**
     * Bloco de inicialização. É executado assim que o ViewModel é criado.
     */
    init {
        checkOnboardingStatus()
    }

    /**
     * Verifica o estado de onboarding do utilizador.
     * Lança uma coroutine para ler o valor do DataStore.
     */
    private fun checkOnboardingStatus() {
        viewModelScope.launch {
            // Usamos .first() porque na Splash Screen só precisamos do primeiro e único valor atual. Não precisamos de observar mudanças.
            val preferences = userPreferencesRepository.userPreferences.first()

            // Atualiza o estado da UI com os dados carregados
            _uiState.update { currentState ->
                currentState.copy(
                    isLoading = false,
                    hasCompletedOnboarding = preferences.hasCompletedOnboarding
                )
            }
        }
    }
}