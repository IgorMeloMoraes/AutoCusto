package com.kingcreationslabs.autocusto.domain.use_case.auth

import com.kingcreationslabs.autocusto.domain.repository.AuthRepository
import javax.inject.Inject

/**
 * Caso de Uso para observar o estado de autenticação.
 * (Camada de Domínio)
 */
class GetAuthStateUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    /**
     * Expõe o Flow do repositório diretamente.
     */
    operator fun invoke() = repository.getAuthState()
}