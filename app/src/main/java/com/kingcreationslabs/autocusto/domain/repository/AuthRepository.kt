package com.kingcreationslabs.autocusto.domain.repository

import com.kingcreationslabs.autocusto.domain.model.AuthUser
import kotlinx.coroutines.flow.Flow

/**
 * Interface (contrato) para a camada de dados de autenticação.
 * Define todas as operações de autenticação permitidas no app, abstraindo a implementação (Firebase, etc.).
 * (Camada de Domínio)
 */
interface AuthRepository {

    /**
     * Observa o estado de autenticação do usuário em tempo real.
     * Emite um [AuthUser] se o usuário estiver logado, ou null se estiver deslogado.
     */
    fun getAuthState(): Flow<AuthUser?>

    /**
     * Tenta realizar login com Email e Senha.
     *
     * @return [Result.success] com [Unit] em caso de sucesso.
     * @return [Result.failure] com uma [Exception] em caso de falha (ex: senha errada).
     */
    suspend fun signInWithEmail(email: String, pass: String): Result<Unit>

    /**
     * Tenta criar uma nova conta com Email e Senha.
     *
     * @return [Result.success] com [Unit] em caso de sucesso.
     * @return [Result.failure] com uma [Exception] em caso de falha (ex: email já em uso).
     */
    suspend fun registerWithEmail(email: String, pass: String): Result<Unit>

    /**
     * Tenta realizar login usando um ID Token obtido do Google Sign-In Client.
     *
     * @param idToken O token JWT fornecido pelo Google SDK.
     * @return [Result.success] com [Unit] em caso de sucesso.
     * @return [Result.failure] com uma [Exception] em caso de falha.
     */
    suspend fun signInWithGoogle(idToken: String): Result<Unit>

    /**
     * Tenta realizar login usando um Access Token obtido do Facebook SDK.
     *
     * @param accessToken O token fornecido pelo Facebook SDK.
     * @return [Result.success] com [Unit] em caso de sucesso.
     * @return [Result.failure] com uma [Exception] em caso de falha.
     */
    suspend fun signInWithFacebook(accessToken: String): Result<Unit>

    /**
     * Realiza o logout do usuário atual da sessão do Firebase.
     */
    suspend fun logout()
}