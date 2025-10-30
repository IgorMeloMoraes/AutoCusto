package com.kingcreationslabs.autocusto.presentation.screens.register

import app.cash.turbine.test
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.kingcreationslabs.autocusto.domain.repository.FakeAuthRepository
import com.kingcreationslabs.autocusto.domain.use_case.auth.RegisterWithEmailUseCase
import com.kingcreationslabs.autocusto.domain.use_case.validation.ValidateConfirmPasswordUseCase
import com.kingcreationslabs.autocusto.domain.use_case.validation.ValidateEmailUseCase
import com.kingcreationslabs.autocusto.domain.use_case.validation.ValidatePasswordUseCase
import com.kingcreationslabs.autocusto.presentation.navigation.Routes
import com.kingcreationslabs.autocusto.util.MainDispatcherRule
import io.mockk.mockk // Vamos precisar de um mock para a Exceção
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

/**
 * Testes de unidade para o [RegisterViewModel].
 */
@ExperimentalCoroutinesApi
class RegisterViewModelTest {

    // Regra para gerir os dispatchers de Coroutines
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    // Dependências Falsas e Reais
    private lateinit var fakeAuthRepository: FakeAuthRepository
    private lateinit var validateEmailUseCase: ValidateEmailUseCase
    private lateinit var validatePasswordUseCase: ValidatePasswordUseCase
    private lateinit var validateConfirmPasswordUseCase: ValidateConfirmPasswordUseCase
    private lateinit var registerWithEmailUseCase: RegisterWithEmailUseCase

    // A classe que estamos testando
    private lateinit var viewModel: RegisterViewModel

    @Before
    fun setUp() {
        // 1. Instancia o repositório falso e os UseCases puros
        fakeAuthRepository = FakeAuthRepository()
        validateEmailUseCase = ValidateEmailUseCase()
        validatePasswordUseCase = ValidatePasswordUseCase()
        validateConfirmPasswordUseCase = ValidateConfirmPasswordUseCase()

        // 2. Instancia o UseCase de registro, injetando o repositório falso
        registerWithEmailUseCase = RegisterWithEmailUseCase(fakeAuthRepository)

        // 3. Instancia o ViewModel, injetando todas as dependências
        viewModel = RegisterViewModel(
            validateEmailUseCase = validateEmailUseCase,
            validatePasswordUseCase = validatePasswordUseCase,
            validateConfirmPasswordUseCase = validateConfirmPasswordUseCase,
            registerWithEmailUseCase = registerWithEmailUseCase
        )
    }

    // --- Testes de Validação em Tempo Real ---

    @Test
    fun `onEvent ConfirmPasswordChanged - senhas diferentes - uiState é atualizado com erro`() = runTest {
        // Preparação (Arrange)
        viewModel.onEvent(RegisterEvent.PasswordChanged("123456"))

        // Ação (Act)
        viewModel.onEvent(RegisterEvent.ConfirmPasswordChanged("654321"))

        // Verificação (Assert)
        val uiState = viewModel.uiState.value
        assertEquals("654321", uiState.confirmPassword)
        assertNotNull(uiState.confirmPasswordError)
        assertEquals("As senhas não coincidem.", uiState.confirmPasswordError)
    }

    @Test
    fun `onEvent ConfirmPasswordChanged - senhas iguais - uiState é atualizado sem erro`() = runTest {
        // Preparação (Arrange)
        viewModel.onEvent(RegisterEvent.PasswordChanged("123456"))

        // Ação (Act)
        viewModel.onEvent(RegisterEvent.ConfirmPasswordChanged("123456"))

        // Verificação (Assert)
        val uiState = viewModel.uiState.value
        assertEquals("123456", uiState.confirmPassword)
        assertNull(uiState.confirmPasswordError) // Erro deve ser nulo
    }

    // --- Testes de Lógica de Registo (Clique) ---

    @Test
    fun `onEvent RegisterClicked - campos inválidos (senhas diferentes) - exibe erros e não faz registo`() = runTest {
        // Preparação (Arrange) - Preenche com dados válidos, exceto confirmação
        viewModel.onEvent(RegisterEvent.EmailChanged("teste@teste.com"))
        viewModel.onEvent(RegisterEvent.PasswordChanged("123456"))
        viewModel.onEvent(RegisterEvent.ConfirmPasswordChanged("654321")) // Senha errada

        // Ação (Act)
        viewModel.onEvent(RegisterEvent.RegisterClicked)

        // Verificação (Assert)
        val uiState = viewModel.uiState.value
        assertFalse(uiState.isLoading) // Não deve carregar
        assertNull(uiState.emailError) // E-mail está ok
        assertNull(uiState.passwordError) // Senha está ok
        assertNotNull(uiState.confirmPasswordError) // Erro de confirmação DEVE aparecer
    }

    @Test
    fun `onEvent RegisterClicked - dados válidos e registo SUCESSO - dispara evento de navegação`() = runTest {
        // Preparação (Arrange) - Preenche com dados válidos
        viewModel.onEvent(RegisterEvent.EmailChanged("teste@teste.com"))
        viewModel.onEvent(RegisterEvent.PasswordChanged("123456"))
        viewModel.onEvent(RegisterEvent.ConfirmPasswordChanged("123456"))

        // Garante que o repositório falso irá retornar SUCESSO
        fakeAuthRepository.setShouldReturnError(false)

        // Ação (Act)
        // Usamos o Turbine para "escutar" o Channel de navegação
        viewModel.navigationEvent.test {

            viewModel.onEvent(RegisterEvent.RegisterClicked) // Dispara o evento

            // Verificação (Assert)
            val uiStateFinal = viewModel.uiState.value

            // 1. Verifica o estado da UI
            assertFalse(uiStateFinal.isLoading) // Loading deve ter terminado
            assertNull(uiStateFinal.generalError) // Nenhum erro geral

            // 2. Verifica o evento de navegação (usando Turbine)
            val navEvent = awaitItem() // Espera o próximo evento do Channel
            assertEquals(Routes.MainApp, navEvent) // Verifica se navegou para o app principal
        }
    }

    @Test
    fun `onEvent RegisterClicked - dados válidos e registo FALHA (Email em Uso) - exibe erro geral`() = runTest {
        // Preparação (Arrange) - Preenche com dados válidos
        viewModel.onEvent(RegisterEvent.EmailChanged("teste@teste.com"))
        viewModel.onEvent(RegisterEvent.PasswordChanged("123456"))
        viewModel.onEvent(RegisterEvent.ConfirmPasswordChanged("123456"))

        // Garante que o repositório falso irá retornar FALHA
        // (Não precisamos simular a exceção exata, o fake já retorna IOException por padrão)
        fakeAuthRepository.setShouldReturnError(true)

        // **NOTA AVANÇADA**: Se quiséssemos testar a MENSAGEM de erro específica
        // (ex: "E-mail já em uso"), teríamos que modificar o FakeAuthRepository
        // para aceitar e atirar uma exceção específica (ex: FirebaseAuthUserCollisionException).
        // Por agora, testar se 'generalError' não é nulo é suficiente.

        // Ação (Act)
        viewModel.onEvent(RegisterEvent.RegisterClicked)

        // Verificação (Assert)
        val uiStateFinal = viewModel.uiState.value
        assertFalse(uiStateFinal.isLoading) // Loading deve ter terminado
        assertNotNull(uiStateFinal.generalError) // Erro geral DEVE estar presente

        // Vamos verificar a mensagem genérica (já que o Fake atira IOException)
        assertEquals("Ocorreu um erro. Tente novamente.", uiStateFinal.generalError)
    }
}