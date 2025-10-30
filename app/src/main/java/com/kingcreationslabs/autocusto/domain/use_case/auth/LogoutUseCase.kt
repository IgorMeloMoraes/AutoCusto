package com.kingcreationslabs.autocusto.domain.use_case.auth

import com.kingcreationslabs.autocusto.domain.repository.AuthRepository
import javax.inject.Inject

/**
 * Caso de Uso para fazer logout.
 * (Camada de Domínio)
 */
class LogoutUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    /**
     * Executa o caso de uso.
     */
    suspend operator fun invoke() = repository.logout()
}