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
import kotlinx.coroutines.delay

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
     * Verifica o estado de onboarding, garantindo um tempo mínimo
     * de exibição da splash screen para fins de branding.
     */
    private fun checkOnboardingStatus() {
        viewModelScope.launch {
            // Define o tempo mínimo de exibição (ex: 2500ms = 3.5 segundos)
            val minDisplayTime = 3500L
            val startTime = System.currentTimeMillis()

            // Usamos .first() porque na Splash Screen só precisamos do primeiro e único valor atual. Não precisamos de observar mudanças.
            // Faz o trabalho real (ler o DataStore)
            val preferences = userPreferencesRepository.userPreferences.first()

            // Calcula quanto tempo o trabalho demorou
            val jobDuration = System.currentTimeMillis() - startTime

            // Se o trabalho foi MAIS RÁPIDO que o tempo mínimo...
            if (jobDuration < minDisplayTime) {
                // ...espera o tempo restante.
                val remainingTime = minDisplayTime - jobDuration
                delay(remainingTime)
            }

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