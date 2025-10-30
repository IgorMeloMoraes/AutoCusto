package com.kingcreationslabs.autocusto.domain.use_case.validation

import javax.inject.Inject

/**
 * Caso de Uso para validar uma senha.
 * (Camada de Domínio)
 */
class ValidatePasswordUseCase @Inject constructor() {

    /**
     * Executa o caso de uso.
     * @param password A senha a ser validada.
     * @return [ValidationResult] O resultado da validação.
     */
    operator fun invoke(password: String): ValidationResult {
        if (password.isBlank()) {
            return ValidationResult(
                isSuccess = false,
                errorMessage = "A senha não pode estar vazia."
            )
        }
        // Regra do Firebase: mínimo de 6 caracteres
        if (password.length < 6) {
            return ValidationResult(
                isSuccess = false,
                errorMessage = "A senha deve ter pelo menos 6 caracteres."
            )
        }
        return ValidationResult(isSuccess = true)
    }
}