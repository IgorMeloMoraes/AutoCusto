package com.kingcreationslabs.autocusto.domain.use_case.auth

import com.kingcreationslabs.autocusto.domain.repository.AuthRepository
import javax.inject.Inject

/**
 * Caso de Uso para fazer login com Google.
 * (Camada de Domínio)
 */
class SignInWithGoogleUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    /**
     * Executa o caso de uso.
     */
    suspend operator fun invoke(idToken: String) =
        repository.signInWithGoogle(idToken)
}