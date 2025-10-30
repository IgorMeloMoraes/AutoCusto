package com.kingcreationslabs.autocusto.domain.use_case.auth

import com.kingcreationslabs.autocusto.domain.model.AuthUser
import com.kingcreationslabs.autocusto.domain.repository.FakeAuthRepository
import com.kingcreationslabs.autocusto.util.MainDispatcherRule // Importa nossa Regra de Teste da Sprint 1
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Testes de unidade para os Casos de Uso de Autenticação.
 * Usamos [runTest] para testar funções 'suspend'.
 */
@ExperimentalCoroutinesApi
class AuthUseCasesTest {

    // Aplica a regra que substitui o Dispatcher.Main (da Sprint 1)
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    // Dependências
    private lateinit var fakeRepository: FakeAuthRepository

    // Instâncias dos UseCases que estamos testando
    private lateinit var getAuthStateUseCase: GetAuthStateUseCase
    private lateinit var registerWithEmailUseCase: RegisterWithEmailUseCase
    private lateinit var signInWithEmailUseCase: SignInWithEmailUseCase
    private lateinit var logoutUseCase: LogoutUseCase
    // (Opcional: podemos adicionar os de Google/Facebook se necessário)

    @Before
    fun setUp() {
        // 1. Criamos o nosso repositório falso
        fakeRepository = FakeAuthRepository()

        // 2. Criamos os UseCases, injetando manualmente o repositório falso
        getAuthStateUseCase = GetAuthStateUseCase(fakeRepository)
        registerWithEmailUseCase = RegisterWithEmailUseCase(fakeRepository)
        signInWithEmailUseCase = SignInWithEmailUseCase(fakeRepository)
        logoutUseCase = LogoutUseCase(fakeRepository)
    }

    @Test
    fun `GetAuthStateUseCase - repositório emite usuário - use case emite usuário`() = runTest {
        // Preparação (Arrange)
        val testUser = AuthUser(uid = "123", email = "teste@teste.com")
        fakeRepository.setAuthState(testUser) // Força o fake a emitir este usuário

        // Ação (Act)
        val emittedUser = getAuthStateUseCase().first() // Coleta o primeiro valor do Flow

        // Verificação (Assert)
        assertNotNull(emittedUser)
        assertEquals("123", emittedUser?.uid)
    }

    @Test
    fun `RegisterWithEmailUseCase - repositório falha - retorna falha`() = runTest {
        // Preparação (Arrange)
        fakeRepository.setShouldReturnError(true) // Força o fake a falhar

        // Ação (Act)
        val result = registerWithEmailUseCase("email", "senha")

        // Verificação (Assert)
        assertTrue(result.isFailure)
    }

    @Test
    fun `RegisterWithEmailUseCase - repositório tem sucesso - retorna sucesso`() = runTest {
        // Preparação (Arrange)
        fakeRepository.setShouldReturnError(false) // Garante que o fake terá sucesso

        // Ação (Act)
        val result = registerWithEmailUseCase("teste@teste.com", "123456")

        // Verificação (Assert)
        assertTrue(result.isSuccess)
    }

    @Test
    fun `LogoutUseCase - executa - estado de auth no repositório fica nulo`() = runTest {
        // Preparação (Arrange)
        val testUser = AuthUser(uid = "123", email = "teste@teste.com")
        fakeRepository.setAuthState(testUser) // Simula um usuário logado

        // Ação (Act)
        logoutUseCase()
        val stateAfterLogout = getAuthStateUseCase().first() // Pega o estado após o logout

        // Verificação (Assert)
        assertEquals(null, stateAfterLogout)
    }
}