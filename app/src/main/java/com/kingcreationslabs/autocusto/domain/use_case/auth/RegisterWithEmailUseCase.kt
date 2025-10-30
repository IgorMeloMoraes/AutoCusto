package com.kingcreationslabs.autocusto.domain.use_case.auth

import com.kingcreationslabs.autocusto.domain.repository.AuthRepository
import javax.inject.Inject

/**
 * Caso de Uso para registrar um novo usuário com e-mail e senha.
 * (Camada de Domínio)
 */
class RegisterWithEmailUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    /**
     * Executa o caso de uso.
     */
    suspend operator fun invoke(email: String, pass: String) =
        repository.registerWithEmail(email, pass)
}