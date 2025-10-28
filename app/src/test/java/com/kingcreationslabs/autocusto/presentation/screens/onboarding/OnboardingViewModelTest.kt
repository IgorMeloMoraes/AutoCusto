package com.kingcreationslabs.autocusto.presentation.screens.onboarding

import com.kingcreationslabs.autocusto.domain.repository.FakeUserPreferencesRepository
import com.kingcreationslabs.autocusto.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Testes de unidade para o [OnboardingViewModel].
 */
@ExperimentalCoroutinesApi
class OnboardingViewModelTest {

    // Aplica a regra que substitui o Dispatcher.Main
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    // Instâncias que vamos usar no teste
    private lateinit var fakeRepository: FakeUserPreferencesRepository
    private lateinit var viewModel: OnboardingViewModel

    // Bloco @Before: Roda antes de CADA teste
    @Before
    fun setUp() {
        // 1. Criamos o nosso repositório falso
        fakeRepository = FakeUserPreferencesRepository()

        // 2. Criamos o ViewModel, injetando manualmente o repositório falso
        viewModel = OnboardingViewModel(fakeRepository)
    }

    @Test
    fun `quando setOnboardingCompleted é chamado, salva true no repositório`() = runTest {
        // --- Verificação Prévia (Opcional, mas boa prática) ---
        // Garante que o estado inicial é false
        var initialPrefs = fakeRepository.userPreferences.first()
        assertEquals(false, initialPrefs.hasCompletedOnboarding)

        // --- Ação (Act) ---
        // Chamamos a função que queremos testar
        viewModel.setOnboardingCompleted()

        // --- Verificação (Assert) ---
        // Verificamos se o valor DENTRO do repositório foi alterado para true
        val finalPrefs = fakeRepository.userPreferences.first()
        assertEquals(true, finalPrefs.hasCompletedOnboarding)
    }
}