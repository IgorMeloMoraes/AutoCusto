package com.kingcreationslabs.autocusto.domain.use_case.validation

import javax.inject.Inject

/**
 * Caso de Uso para validar se a senha e a confirmação de senha são iguais.
 * (Camada de Domínio)
 */
class ValidateConfirmPasswordUseCase @Inject constructor() {

    /**
     * Executa o caso de uso.
     * @param password A senha original.
     * @param confirmPassword A senha de confirmação.
     * @return [ValidationResult] O resultado da validação.
     */
    operator fun invoke(password: String, confirmPassword: String): ValidationResult {
        if (password != confirmPassword) {
            return ValidationResult(
                isSuccess = false,
                errorMessage = "As senhas não coincidem."
            )
        }
        return ValidationResult(isSuccess = true)
    }
}