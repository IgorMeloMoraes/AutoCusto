package com.kingcreationslabs.autocusto.presentation.screens.splash

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
 * Testes de unidade para o [SplashViewModel].
 */
@ExperimentalCoroutinesApi
class SplashViewModelTest {

    // Aplica a regra que substitui o Dispatcher.Main
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    // O Repositório Falso é inicializado antes de cada teste
    private lateinit var fakeRepository: FakeUserPreferencesRepository

    // O ViewModel será inicializado DENTRO de cada teste
    private lateinit var viewModel: SplashViewModel

    @Before
    fun setUp() {
        // No setUp, APENAS inicializamos o repositório falso
        fakeRepository = FakeUserPreferencesRepository()
    }

    @Test
    fun `quando ViewModel é inicializado E onboarding FOI completo, estado é carregado como true`() = runTest {
        // --- Preparação (Arrange) ---
        // 1. Definimos o estado que queremos que o repositório falso retorne
        val expectedOnboardingStatus = true
        fakeRepository.setInitialOnboardingStatus(expectedOnboardingStatus)

        // --- Ação (Act) ---
        // 2. CRIAMOS o ViewModel. O bloco 'init' roda AGORA, lendo o estado 'true'.
        viewModel = SplashViewModel(fakeRepository)

        // --- Verificação (Assert) ---
        // 3. Lemos o estado resultante
        val uiState = viewModel.uiState.first()

        // 4. Verificamos se o estado da UI reflete os dados do repositório
        assertEquals(false, uiState.isLoading)
        assertEquals(expectedOnboardingStatus, uiState.hasCompletedOnboarding)
    }

    @Test
    fun `quando ViewModel é inicializado E onboarding NÃO foi completo, estado é carregado como false`() = runTest {
        // --- Preparação (Arrange) ---
        // 1. O estado padrão do Fake já é 'false',
        //    então não precisamos chamar 'setInitialOnboardingStatus'.
        val expectedOnboardingStatus = false

        // --- Ação (Act) ---
        // 2. CRIAMOS o ViewModel. O bloco 'init' roda AGORA, lendo o estado 'false'.
        viewModel = SplashViewModel(fakeRepository)

        // --- Verificação (Assert) ---
        // 3. Lemos o estado resultante
        val uiState = viewModel.uiState.first()

        // 4. Verificamos se o estado da UI reflete os dados do repositório
        assertEquals(false, uiState.isLoading)
        assertEquals(expectedOnboardingStatus, uiState.hasCompletedOnboarding)
    }
}