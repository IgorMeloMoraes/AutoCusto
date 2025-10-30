package com.kingcreationslabs.autocusto.domain.repository

import com.kingcreationslabs.autocusto.domain.model.AuthUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.IOException
import javax.inject.Inject

/**
 * Implementação falsa (fake) do [AuthRepository] para uso em testes de unidade.
 * (Fonte de Teste)
 *
 * Permite-nos controlar diretamente o estado de autenticação e simular
 * sucessos ou falhas nas chamadas de login/registro.
 */
class FakeAuthRepository @Inject constructor() : AuthRepository { // @Inject para testes de Hilt futuros

    // Controla o que getAuthState() emite
    private val _authState = MutableStateFlow<AuthUser?>(null)

    // Controla se as próximas chamadas (signIn, register) devem falhar
    private var shouldReturnError = false

    /**
     * Define o estado de erro para os próximos testes.
     * @param shouldError Se true, as próximas chamadas de login/registro falharão.
     */
    fun setShouldReturnError(shouldError: Boolean) {
        shouldReturnError = shouldError
    }

    /**
     * Define o estado de login "falso" que será emitido.
     * @param user O usuário a ser emitido, ou null para simular logout.
     */
    fun setAuthState(user: AuthUser?) {
        _authState.value = user
    }

    override fun getAuthState(): Flow<AuthUser?> {
        return _authState.asStateFlow()
    }

    override suspend fun signInWithEmail(email: String, pass: String): Result<Unit> {
        return if (shouldReturnError) {
            Result.failure(IOException("Erro simulado de login."))
        } else {
            // Simula sucesso de login e atualiza o estado
            setAuthState(AuthUser(uid = "test_uid", email = email))
            Result.success(Unit)
        }
    }

    override suspend fun registerWithEmail(email: String, pass: String): Result<Unit> {
        return if (shouldReturnError) {
            Result.failure(IOException("Erro simulado de registro."))
        } else {
            // Simula sucesso de registro e atualiza o estado
            setAuthState(AuthUser(uid = "test_uid", email = email))
            Result.success(Unit)
        }
    }

    // Não precisamos simular Google/Facebook em detalhes por enquanto
    override suspend fun signInWithGoogle(idToken: String): Result<Unit> {
        return if (shouldReturnError) Result.failure(IOException()) else Result.success(Unit)
    }

    override suspend fun signInWithFacebook(accessToken: String): Result<Unit> {
        return if (shouldReturnError) Result.failure(IOException()) else Result.success(Unit)
    }

    override suspend fun logout() {
        setAuthState(null) // Simula o logout
        // Na implementação real, não precisamos de Result, mas no fake é útil
    }
}