package com.kingcreationslabs.autocusto.presentation.screens.login

import app.cash.turbine.test
import com.kingcreationslabs.autocusto.domain.repository.FakeAuthRepository
import com.kingcreationslabs.autocusto.domain.use_case.auth.SignInWithEmailUseCase
import com.kingcreationslabs.autocusto.domain.use_case.validation.ValidateEmailUseCase
import com.kingcreationslabs.autocusto.domain.use_case.validation.ValidatePasswordUseCase
import com.kingcreationslabs.autocusto.presentation.navigation.Routes
import com.kingcreationslabs.autocusto.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

/**
 * Testes de unidade para o [LoginViewModel].
 */
@ExperimentalCoroutinesApi
class LoginViewModelTest {

    // Regra para gerenciar os dispatchers de Coroutines
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    // Dependências Falsas e Reais
    private lateinit var fakeAuthRepository: FakeAuthRepository
    private lateinit var validateEmailUseCase: ValidateEmailUseCase
    private lateinit var validatePasswordUseCase: ValidatePasswordUseCase
    private lateinit var signInWithEmailUseCase: SignInWithEmailUseCase

    // A classe que estamos testando
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setUp() {
        // 1. Instancia o repositório falso e os UseCases puros
        fakeAuthRepository = FakeAuthRepository()
        validateEmailUseCase = ValidateEmailUseCase() // Real, pois é puro
        validatePasswordUseCase = ValidatePasswordUseCase() // Real, pois é puro

        // 2. Instancia o UseCase de login, injetando o repositório falso
        signInWithEmailUseCase = SignInWithEmailUseCase(fakeAuthRepository)

        // 3. Instancia o ViewModel, injetando todas as dependências
        viewModel = LoginViewModel(
            validateEmailUseCase = validateEmailUseCase,
            validatePasswordUseCase = validatePasswordUseCase,
            signInWithEmailUseCase = signInWithEmailUseCase
        )
    }

    // --- Testes de Validação em Tempo Real (Eventos de Mudança) ---

    @Test
    fun `onEvent EmailChanged - e-mail inválido - uiState é atualizado com erro`() = runTest {
        // Ação (Act)
        viewModel.onEvent(LoginEvent.EmailChanged("email-invalido.com"))

        // Verificação (Assert)
        val uiState = viewModel.uiState.value
        assertEquals("email-invalido.com", uiState.email)
        assertNotNull(uiState.emailError) // Verifica se a mensagem de erro apareceu
    }

    @Test
    fun `onEvent EmailChanged - e-mail válido - uiState é atualizado sem erro`() = runTest {
        // Ação (Act)
        viewModel.onEvent(LoginEvent.EmailChanged("email@valido.com"))

        // Verificação (Assert)
        val uiState = viewModel.uiState.value
        assertEquals("email@valido.com", uiState.email)
        assertNull(uiState.emailError) // Verifica se o erro está nulo
    }

    @Test
    fun `onEvent PasswordChanged - senha curta - uiState é atualizado com erro`() = runTest {
        // Ação (Act)
        viewModel.onEvent(LoginEvent.PasswordChanged("123"))

        // Verificação (Assert)
        val uiState = viewModel.uiState.value
        assertEquals("123", uiState.password)
        assertNotNull(uiState.passwordError)
        assertEquals("A senha deve ter pelo menos 6 caracteres.", uiState.passwordError)
    }

    // --- Testes de Lógica de Login (Clique) ---

    @Test
    fun `onEvent LoginClicked - campos inválidos - exibe erros e não faz login`() = runTest {
        // Preparação (Arrange) - Preenche o ViewModel com dados inválidos
        viewModel.onEvent(LoginEvent.EmailChanged("email-invalido"))
        viewModel.onEvent(LoginEvent.PasswordChanged("123"))

        // Ação (Act)
        viewModel.onEvent(LoginEvent.LoginClicked)

        // Verificação (Assert)
        val uiState = viewModel.uiState.value
        assertFalse(uiState.isLoading) // Não deve carregar
        assertNotNull(uiState.emailError) // Deve exibir o erro do e-mail
        assertNotNull(uiState.passwordError) // Deve exibir o erro da senha

        // (Opcional, mas boa prática: verificar se o repo foi chamado)
        // Como o repo é fake, podemos checar seu estado interno, mas não é necessário aqui.
    }

    @Test
    fun `onEvent LoginClicked - dados válidos e login SUCESSO - dispara evento de navegação`() = runTest {
        // Preparação (Arrange) - Preenche com dados válidos
        viewModel.onEvent(LoginEvent.EmailChanged("teste@teste.com"))
        viewModel.onEvent(LoginEvent.PasswordChanged("123456"))

        // Garante que o repositório falso irá retornar SUCESSO
        fakeAuthRepository.setShouldReturnError(false)

        // Ação (Act)
        // Precisamos testar o uiState E o navigationEvent ao mesmo tempo
        viewModel.navigationEvent.test { // Inicia o "escutador" do Turbine

            viewModel.onEvent(LoginEvent.LoginClicked) // Dispara o evento

            // Verificação (Assert)
            val uiStateFinal = viewModel.uiState.value

            // 1. Verifica o estado da UI
            assertFalse(uiStateFinal.isLoading) // Loading deve ter terminado
            assertNull(uiStateFinal.generalError) // Nenhum erro geral

            // 2. Verifica o evento de navegação (usando Turbine)
            val navEvent = awaitItem() // Espera o próximo evento do Channel
            assertEquals(Routes.MainApp, navEvent) // Verifica se é o evento correto
        }
    }

    @Test
    fun `onEvent LoginClicked - dados válidos e login FALHA - exibe erro geral`() = runTest {
        // Preparação (Arrange) - Preenche com dados válidos
        viewModel.onEvent(LoginEvent.EmailChanged("teste@teste.com"))
        viewModel.onEvent(LoginEvent.PasswordChanged("123456"))

        // Garante que o repositório falso irá retornar FALHA
        fakeAuthRepository.setShouldReturnError(true)

        // Ação (Act)
        viewModel.onEvent(LoginEvent.LoginClicked)

        // Verificação (Assert)
        val uiStateFinal = viewModel.uiState.value
        assertFalse(uiStateFinal.isLoading) // Loading deve ter terminado
        assertNotNull(uiStateFinal.generalError) // Erro geral DEVE estar presente
        assertEquals("E-mail ou senha inválidos. Tente novamente.", uiStateFinal.generalError)
    }
}