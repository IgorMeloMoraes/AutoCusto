package com.kingcreationslabs.autocusto.presentation.screens.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kingcreationslabs.autocusto.domain.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para a [OnboardingScreen].
 *
 * Responsável por gerir as ações do utilizador na tela de onboarding,
 * como salvar a conclusão do processo.
 *
 * @property userPreferencesRepository O repositório para salvar as preferências.
 */
@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    /**
     * Sinaliza ao repositório que o utilizador concluiu
     * o fluxo de onboarding.
     *
     * Esta é uma operação 'fire-and-forget' (dispare-e-esqueça)
     * lançada numa coroutine.
     */
    fun setOnboardingCompleted() {
        viewModelScope.launch {
            userPreferencesRepository.setHasCompletedOnboarding(completed = true)
        }
    }
}